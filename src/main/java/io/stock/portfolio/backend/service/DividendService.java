package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.yahoo.YahooApiClient;
import io.stock.portfolio.backend.controller.model.DividendResponse;
import io.stock.portfolio.backend.database.model.DividendEntity;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.repository.DividendRepository;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DividendService {

    private final DividendRepository dividendRepository;
    private final PositionRepository positionRepository;
    private final YahooApiClient yahooApiClient;

    public List<DividendResponse> getDividendsBySymbolAndOwner(String symbol, String owner) {
        // Filter dividends also by first buy date
        List<DividendEntity> dividends = dividendRepository.findBySymbol(symbol);

        List<DividendEntity> dividendsSortedByPaymentDate = dividends
                .stream()
                .sorted(Comparator.comparing(DividendEntity::getExDate).reversed())
                .collect(toList());

        if (!dividendsSortedByPaymentDate.isEmpty()) {
            LocalDateTime lastDividend = dividendsSortedByPaymentDate.get(0).getExDate();
            if (lastDividend.plusMonths(1).isAfter(LocalDateTime.now())) {
                dividendsSortedByPaymentDate.addAll(retrieveAndSaveDividends(symbol, lastDividend));
            }
        } else {
            List<PositionEntity> positionsOrderedByBueDate = positionRepository.findBySymbolAndOwner(symbol, owner)
                    .stream()
                    .sorted(Comparator.comparing(PositionEntity::getBuyDate).reversed())
                    .collect(toList());
            dividendsSortedByPaymentDate.addAll(retrieveAndSaveDividends(symbol, positionsOrderedByBueDate.get(0).getBuyDate()));
        }
        return dividendsSortedByPaymentDate
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Map<String, Float> getTotalEuroNettoToSymbol() {
        Map<String, Float> positionToDividends = dividendRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        DividendEntity::getSymbol, DividendEntity::getEuroBruttoAmount, Float::sum));

        positionToDividends.entrySet()
                .forEach(entry -> entry.setValue(calculateAustrianTax(entry.getValue())));
        return positionToDividends;
    }

    private DividendResponse convertToResponse(DividendEntity dividendEntity) {
        return new DividendResponse()
                // .setAmountPerShare(dividendEntity.getAmountPerShare())
                .setDollarBruttoAmount(dividendEntity.getDollarBruttoAmount())
                .setEuroBruttoAmount(dividendEntity.getEuroBruttoAmount())
                .setSymbol(dividendEntity.getSymbol())
                .setExDate(dividendEntity.getExDate())
                .setDollarNettoAmount(calculateUSATax(dividendEntity.getDollarBruttoAmount()))
                .setEuroNettoAmount(calculateAustrianTax(dividendEntity.getEuroBruttoAmount()));
    }

    private Float calculateAustrianTax(Float euroBruttoAmount) {
// TODO return reql value
        return euroBruttoAmount * 0.7f;
    }

    private Float calculateUSATax(Float dollarBruttoAmount) {
        //15% Quellensteuer
        return dollarBruttoAmount * 0.85f;
    }

    private List<DividendEntity> retrieveAndSaveDividends(String symbol, LocalDateTime lastDividendDate) {
        List<DividendEntity> dividendEntities = yahooApiClient.detDividends(symbol, lastDividendDate)
                .stream()
                .map(div -> {
                    DividendEntity dividendEntity = new DividendEntity();
                    dividendEntity.setSymbol(symbol);
                    dividendEntity.setExDate(div.getExDate());
                    dividendEntity.setDollarBruttoAmount(div.getAmount());
                    Float dollarNetto = calculateUSATax(div.getAmount());
                    //TODO Call exchangeRateService Float euroBrutto
                    Float euroBrutto = dollarNetto;
                    dividendEntity.setEuroBruttoAmount(calculateAustrianTax(euroBrutto));
                    return dividendEntity;
                })
                .collect(toList());

        dividendRepository.saveAll(dividendEntities);

        return dividendEntities;

    }

}
