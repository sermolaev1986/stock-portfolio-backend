package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.yahoo.YahooApiClient;
import io.stock.portfolio.backend.client.yahoo.YahooDividend;
import io.stock.portfolio.backend.controller.model.DividendResponse;
import io.stock.portfolio.backend.database.model.DividendEntity;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.model.TransactionEntity;
import io.stock.portfolio.backend.database.repository.DividendRepository;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import io.stock.portfolio.backend.database.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DividendService {

    private final DividendRepository dividendRepository;
    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;

    private final YahooApiClient yahooApiClient;

    public List<DividendResponse> getDividendsBySymbolAndOwner(String symbol, String owner) {
        List<DividendEntity> dividends = dividendRepository.findBySymbolAndOwner(symbol, owner);

        List<DividendEntity> dividendsSortedByPaymentDate = dividends
                .stream()
                .sorted(Comparator.comparing(DividendEntity::getExDate).reversed())
                .collect(toList());

        if (!dividendsSortedByPaymentDate.isEmpty()) {
            LocalDateTime lastDividend = dividendsSortedByPaymentDate.get(0).getExDate();
            if (lastDividend.plusMonths(1).isAfter(LocalDateTime.now())) {
                dividendsSortedByPaymentDate.addAll(retrieveAndSaveDividends(symbol, owner, lastDividend));
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

    public Float getTotalDividendsEuroNetto(String symbol, String owner) {
        return getDividendsBySymbolAndOwner(symbol, owner)
                .stream()
                .map(DividendResponse::getEuroBruttoAmount)
                .reduce(Float::sum)
                .orElse(0.0f);
    }

    private DividendResponse convertToResponse(DividendEntity dividendEntity) {
        return new DividendResponse()
                .setDollarBruttoAmount(dividendEntity.getDollarBruttoAmount())
                .setEuroBruttoAmount(dividendEntity.getEuroBruttoAmount())
                .setShareAmount(dividendEntity.getShareAmount())
                .setSymbol(dividendEntity.getSymbol())
                .setExDate(dividendEntity.getExDate())
                .setPaymentDate(dividendEntity.getExDate())
                .setDollarNettoAmount(calculateUSATax(dividendEntity.getDollarBruttoAmount()))
                .setEuroNettoAmount(calculateAustrianTax(dividendEntity.getEuroBruttoAmount()));
    }

    private Float calculateAustrianTax(Float euroBruttoAmount) {
        //12,5% KEST
        return euroBruttoAmount * 0.875f;
    }

    private Float calculateUSATax(Float dollarBruttoAmount) {
        //15% Quellensteuer
        return dollarBruttoAmount * 0.85f;
    }

    private List<DividendEntity> retrieveAndSaveDividends(String symbol, String owner, LocalDateTime lastDividendDate) {
        List<YahooDividend> yahooDividendsSorted = yahooApiClient.detDividends(symbol, lastDividendDate)
                .stream()
                .sorted(Comparator.comparing(YahooDividend::getExDate))
                .collect(toList());

        List<TransactionEntity> transactionsSorted = transactionRepository.findBySymbolAndOwnerOrderByDateAsc(symbol, owner);


        List<PeriodWithAmount> periods = new ArrayList<>();
        int currentAmountOfShares = 0;

        if (transactionsSorted.size() > 2) {
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
                        .map(div -> {
                            DividendEntity dividendEntity = new DividendEntity()
                                    .setSymbol(symbol)
                                    .setOwner(owner)
                                    .setExDate(div.getExDate())
                                    .setShareAmount(period.getAmountOfShares())
                                    .setDollarBruttoAmount(div.getAmount() * period.amountOfShares)
                                    .setEuroBruttoAmount(0.0f);
                            Float dollarNetto = calculateUSATax(div.getAmount());
                            //TODO Call exchangeRateService Float euroBrutto
                            Float euroBrutto = dollarNetto;
                            dividendEntity.setEuroBruttoAmount(calculateAustrianTax(euroBrutto));
                            return dividendEntity;
                        }))
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
