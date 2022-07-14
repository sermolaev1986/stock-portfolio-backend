package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.exchangerate.ExchangeRateClient;
import io.stock.portfolio.backend.client.yahoo.YahooApiClient;
import io.stock.portfolio.backend.client.yahoo.YahooDividend;
import io.stock.portfolio.backend.client.yahoo.YahooDividendsAndSplits;
import io.stock.portfolio.backend.controller.model.DividendResponse;
import io.stock.portfolio.backend.controller.model.SymbolOwner;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DividendService {

    private final TransactionService transactionService;

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
            if (lastDividend.plusDays(20).isBefore(LocalDateTime.now())) {
                dividendsSortedByPaymentDate.addAll(retrieveAndSaveDividends(symbol, owner, lastDividend.plusDays(1)));
            }
        } else {
            PositionEntity position = positionRepository.findByStockEuSymbolAndOwner(symbol, owner)
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

    public void updateSplitTransactions(String symbol, String owner, YahooDividendsAndSplits dividendsAndSplits) {

        var newSplitTransactions = dividendsAndSplits.getSplits().stream().map(yahooSplit ->
                new TransactionEntity()
                        .setSymbol(symbol)
                        .setOwner(owner)
                        .setDate(yahooSplit.getDate())
                        .setOperator(Operator.MULTIPLY)
                        //TODO will not work for reverse splits
                        .setArgument(yahooSplit.getMultiplier())
        ).collect(Collectors.toSet());

        transactionService.saveOrUpdateTransactions(newSplitTransactions);
    }

    private DividendResponse convertToResponse(DividendEntity dividendEntity) {

        BigDecimal dollarBruttoAmount = dividendEntity.getAmountPerShare().multiply(dividendEntity.getShareAmount());
        BigDecimal euroBruttoAmount = BigDecimal.ZERO;
        BigDecimal euroNettoAmount = BigDecimal.ZERO;

        if (!dividendEntity.getExchangeRate().stripTrailingZeros().equals(BigDecimal.ZERO)) {
            euroBruttoAmount = dollarBruttoAmount.divide(dividendEntity.getExchangeRate(), RoundingMode.HALF_DOWN);
            euroNettoAmount = calculateAfterTax(euroBruttoAmount);
        }

        return new DividendResponse()
                .setSymbol(dividendEntity.getSymbol())
                .setExDate(dividendEntity.getExDate())
                .setPaymentDate(dividendEntity.getExDate())
                .setShareAmount(dividendEntity.getShareAmount())
                .setAmountPerShare(dividendEntity.getAmountPerShare())
                .setDollarBruttoAmount(dollarBruttoAmount)
                .setEuroBruttoAmount(euroBruttoAmount)
                .setEuroNettoAmount(euroNettoAmount);
    }

    private BigDecimal calculateAfterTax(BigDecimal euroBruttoAmount) {
        //27,5% KEST + Quellensteuer
        return euroBruttoAmount.multiply(new BigDecimal("0.725"));
    }

    private List<DividendEntity> retrieveAndSaveDividends(String symbol, String owner, LocalDateTime lastDividendDate) {

        var maybeResponse = yahooApiClient.getDividendsAndSplits(symbol, lastDividendDate);
        if (maybeResponse.isEmpty()) {
            return Collections.emptyList();
        }
        // Update SPLIT transactions is in the same method as update dividends because of the limits on yahoo api calls
        updateSplitTransactions(symbol, owner, maybeResponse.get());

        List<YahooDividend> yahooDividendsSorted = maybeResponse.get().getDividends()
                .stream()
                .sorted(Comparator.comparing(YahooDividend::getExDate))
                .collect(toList());

        List<TransactionEntity> transactionsSorted = transactionRepository.findBySymbolAndOwnerOrderByDateAsc(symbol, owner);

        List<PeriodWithAmount> periods = new ArrayList<>();
        BigDecimal currentAmountOfShares = BigDecimal.ZERO;

        if (transactionsSorted.size() > 1) {
            for (int i = 0; i < transactionsSorted.size() - 1; i++) {
                TransactionEntity first = transactionsSorted.get(i);
                TransactionEntity second = transactionsSorted.get(i + 1);
                currentAmountOfShares = first.getOperator().calculateAmountOfShares(currentAmountOfShares, first.getArgument());
                periods.add(new PeriodWithAmount(first.getDate(), second.getDate(), currentAmountOfShares));
            }
        }
        // Add the last period or the only one if no changes after buy were added
        TransactionEntity theOnlyOrTheLast = transactionsSorted.get(transactionsSorted.size() - 1);
        currentAmountOfShares = theOnlyOrTheLast.getOperator().calculateAmountOfShares(currentAmountOfShares, theOnlyOrTheLast.getArgument());
        periods.add(new PeriodWithAmount(theOnlyOrTheLast.getDate(), LocalDateTime.now(), currentAmountOfShares));

        List<PeriodWithAmount> newPeriods = periods
                .stream()
                .filter(per -> per.lastsAfter(lastDividendDate))
                .filter(per -> !per.getAmountOfShares().equals(BigDecimal.ZERO))
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

    @Transactional
    public void updateDividends() {
        Map<SymbolOwner, List<TransactionEntity>> transactionsBySymbolAndOwner = transactionRepository.findAll()
                .stream()
                .collect(groupingBy(tr -> new SymbolOwner(tr.getSymbol(), tr.getOwner())));

        for (Map.Entry<SymbolOwner, List<TransactionEntity>> entry : transactionsBySymbolAndOwner.entrySet()) {
            var lastDividends = dividendRepository.findFirstBySymbolAndOwnerOrderByExDateDesc(entry.getKey().getSymbol(), entry.getKey().getOwner());

            LocalDateTime lastDividendDate;

            if (lastDividends.isPresent()) {
                lastDividendDate = lastDividends.get().getExDate().plusDays(1); // To avoid saving the same dividend 2 times since yahoo returns dividends including start date
            } else {
                List<TransactionEntity> orderedTransactions = entry.getValue();
                orderedTransactions.sort(Comparator.comparing(TransactionEntity::getDate));

                TransactionEntity firstTransaction = orderedTransactions.get(0);
                lastDividendDate = firstTransaction.getDate();
            }

            if (lastDividendDate.plusDays(20).isBefore(LocalDateTime.now())) {
                retrieveAndSaveDividends(entry.getKey().getSymbol(), entry.getKey().getOwner(), lastDividendDate);
            }
        }
    }

    @AllArgsConstructor
    private static class PeriodWithAmount {
        private LocalDateTime from;
        private LocalDateTime to;
        private BigDecimal amountOfShares;

        public BigDecimal getAmountOfShares() {
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
