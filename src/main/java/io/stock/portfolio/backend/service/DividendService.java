package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.exchangerate.ExchangeRateClient;
import io.stock.portfolio.backend.client.yahoo.YahooApiClient;
import io.stock.portfolio.backend.client.yahoo.YahooDividend;
import io.stock.portfolio.backend.controller.model.DividendResponse;
import io.stock.portfolio.backend.database.model.DividendEntity;
import io.stock.portfolio.backend.database.model.Operator;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.model.TransactionEntity;
import io.stock.portfolio.backend.database.repository.DividendRepository;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import io.stock.portfolio.backend.database.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DividendService {

    private final DividendRepository dividendRepository;
    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;

    private final YahooApiClient yahooApiClient;
    private final ExchangeRateClient exchangeRateClient;

    public List<DividendResponse> getDividendsBySymbolAndOwner(String symbol, String owner) {
        return getDividendsBySymbolAndOwner(symbol, owner, dividendRepository.findBySymbolAndOwner(symbol, owner));
    }

    public List<DividendResponse> getDividendsBySymbolAndOwner(String symbol, String owner, List<DividendEntity> dividends) {
        List<DividendEntity> dividendsSortedByPaymentDate = dividends
                .stream()
                .sorted(Comparator.comparing(DividendEntity::getExDate).reversed())
                .collect(toList());

        if (!dividendsSortedByPaymentDate.isEmpty()) {
            LocalDateTime lastDividend = dividendsSortedByPaymentDate.get(0).getExDate();
            if (lastDividend.plusMonths(1).isBefore(LocalDateTime.now())) {
                dividendsSortedByPaymentDate.addAll(retrieveAndSaveDividends(symbol, owner, lastDividend.plusDays(1)));
            }
        } else {
            PositionEntity position = positionRepository.findBySymbolAndOwner(symbol, owner)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Position not found for %s, %s", symbol, owner)));
            dividendsSortedByPaymentDate.addAll(retrieveAndSaveDividends(symbol, owner, position.getBuyDate()));
        }
        return dividendsSortedByPaymentDate
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalDividendsEuroNetto(String symbol, String owner, List<DividendEntity> dividends) {
        return getDividendsBySymbolAndOwner(symbol, owner, dividends)
                .stream()
                .map(DividendResponse::getEuroNettoAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Map<String, List<DividendEntity>> getDividendsByOwner(String owner) {
        return dividendRepository.findByOwner(owner).stream()
                .collect(Collectors.groupingBy(DividendEntity::getSymbol));
    }

    private DividendResponse convertToResponse(DividendEntity dividendEntity) {

        BigDecimal dollarBruttoAmount = dividendEntity.getAmountPerShare().multiply(new BigDecimal(dividendEntity.getShareAmount()));
        BigDecimal dollarNettoAmount = calculateAfterUSATax(dollarBruttoAmount);
        BigDecimal euroBruttoAmount = dollarNettoAmount.divide(dividendEntity.getExchangeRate(), RoundingMode.HALF_DOWN);
        BigDecimal euroNettoAmount = calculateAfterAustrianTax(euroBruttoAmount);

        return new DividendResponse()
                .setSymbol(dividendEntity.getSymbol())
                .setExDate(dividendEntity.getExDate())
                .setPaymentDate(dividendEntity.getExDate())
                .setShareAmount(dividendEntity.getShareAmount())
                .setAmountPerShare(dollarBruttoAmount.divide(new BigDecimal(dividendEntity.getShareAmount()), RoundingMode.HALF_DOWN))
                .setDollarBruttoAmount(dollarBruttoAmount)
                .setEuroBruttoAmount(euroBruttoAmount)
                .setDollarNettoAmount(dollarNettoAmount)
                .setEuroNettoAmount(euroNettoAmount);
    }

    private BigDecimal calculateAfterAustrianTax(BigDecimal euroBruttoAmount) {
        //12,5% KEST
        return euroBruttoAmount.multiply(new BigDecimal("0.875"));
    }

    private BigDecimal calculateAfterUSATax(BigDecimal dollarBruttoAmount) {
        //15% Quellensteuer
        return dollarBruttoAmount.multiply(new BigDecimal("0.85"));
    }

    private List<DividendEntity> retrieveAndSaveDividends(String symbol, String owner, LocalDateTime lastDividendDate) {
        var maybeResponse = yahooApiClient.getDividendsAndSplits(symbol, lastDividendDate);
        if (maybeResponse.isEmpty()) {
            return Collections.emptyList();
        }

        var response = maybeResponse.get();
        var newSplitTransactions = response.getSplits().stream().map(yahooSplit ->
                new TransactionEntity()
                        .setSymbol(symbol)
                        .setOwner(owner)
                        .setDate(yahooSplit.getDate())
                        .setOperator(Operator.MULTIPLY)
                        //TODO will not work for reverse splits
                        .setArgument(yahooSplit.getMultiplier().intValue())
        ).collect(Collectors.toSet());

        transactionRepository.saveAll(newSplitTransactions);

        List<YahooDividend> yahooDividendsSorted = maybeResponse.get().getDividends()
                .stream()
                .sorted(Comparator.comparing(YahooDividend::getExDate))
                .collect(toList());

        List<TransactionEntity> transactionsSorted = transactionRepository.findBySymbolAndOwnerOrderByDateAsc(symbol, owner);

        List<PeriodWithAmount> periods = new ArrayList<>();
        int currentAmountOfShares = 0;

        if (transactionsSorted.size() > 1) {
            for (int i = 0; i < transactionsSorted.size() - 1; i++) {
                TransactionEntity first = transactionsSorted.get(i);
                TransactionEntity second = transactionsSorted.get(i + 1);
                currentAmountOfShares = first.getOperator().calculate(currentAmountOfShares, first.getArgument());
                periods.add(new PeriodWithAmount(first.getDate(), second.getDate(), currentAmountOfShares));
            }
        }
        // Add the last period or the only one if no changes after buy were added
        TransactionEntity theOnlyOrTheLast = transactionsSorted.get(transactionsSorted.size() - 1);
        currentAmountOfShares = theOnlyOrTheLast.getOperator().calculate(currentAmountOfShares, theOnlyOrTheLast.getArgument());
        periods.add(new PeriodWithAmount(theOnlyOrTheLast.getDate(), LocalDateTime.now(), currentAmountOfShares));

        List<PeriodWithAmount> newPeriods = periods
                .stream()
                .filter(per -> per.lastsAfter(lastDividendDate))
                .collect(toList());

        List<DividendEntity> dividendEntities = newPeriods
                .stream()
                .flatMap(period -> yahooDividendsSorted
                        .stream()
                        .filter(div -> period.isInPeriod(div.getExDate()))
                        .map(div -> new DividendEntity()
                                .setSymbol(symbol)
                                .setOwner(owner)
                                .setExDate(div.getExDate())
                                .setShareAmount(period.getAmountOfShares())
                                .setAmountPerShare(div.getAmount())
                                .setExchangeRate(exchangeRateClient.getByDate(div.getExDate()))))
                .collect(toList());

        List<DividendEntity> severStal = dividendEntities.stream()
                .filter(div -> "RTS2".equals(div.getSymbol()))
                .sorted(Comparator.comparing(DividendEntity::getExDate))
                .collect(toList());

        for (int i = 0; i < severStal.size() - 1; i++) { // Because of the severStal bug
            DividendEntity first = severStal.get(i);
            DividendEntity second = severStal.get(i + 1);
            if (first.getExDate().getMonth().equals(second.getExDate().getMonth())) {
                dividendEntities.remove(first);
            }
        }

        dividendRepository.saveAll(dividendEntities);

        return dividendEntities;

    }

    @AllArgsConstructor
    private static class PeriodWithAmount {
        private LocalDateTime from;
        private LocalDateTime to;
        private Integer amountOfShares;

        public Integer getAmountOfShares() {
            return amountOfShares;
        }

        public boolean isInPeriod(LocalDateTime moment) {
            return moment.isAfter(from) && moment.isBefore(to);
        }

        public boolean lastsAfter(LocalDateTime moment) {
            return to.isAfter(moment);
        }

    }

}
