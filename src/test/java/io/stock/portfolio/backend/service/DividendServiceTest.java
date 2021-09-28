package io.stock.portfolio.backend.service;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DividendServiceTest {

    @Mock
    private DividendRepository dividendRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private YahooApiClient yahooApiClient;

    private DividendService dividendService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Test
    void retrieveAndSaveDividendsForTheFirstRun() {
        LocalDateTime firstTransactionDate = LocalDateTime.parse("2020-01-01 06:00:00", formatter);

        when(dividendRepository.findBySymbolAndOwner("APP", "Olga"))
                .thenReturn(Collections.emptyList());

        PositionEntity position = new PositionEntity()
                .setOwner("Olga")
                .setSymbol("APP")
                .setBuyDate(firstTransactionDate);

        when(positionRepository.findBySymbolAndOwner("APP", "Olga"))
                .thenReturn(Optional.of(position));

        TransactionEntity buy = new TransactionEntity()
                .setDate(firstTransactionDate)
                .setArgument(10)
                .setOperator(Operator.PLUS);

        TransactionEntity moreBuy = new TransactionEntity()
                .setDate(LocalDateTime.parse("2020-02-12 06:00:00", formatter))
                .setArgument(3)
                .setOperator(Operator.PLUS);

        TransactionEntity sell = new TransactionEntity()
                .setDate(LocalDateTime.parse("2020-12-10 06:00:00", formatter))
                .setArgument(5)
                .setOperator(Operator.MINUS);

        when(transactionRepository.findBySymbolAndOwnerOrderByDateAsc("APP", "Olga"))
                .thenReturn(List.of(buy, moreBuy, sell));

        YahooDividend div_15_jan = new YahooDividend()
                .setExDate(LocalDateTime.parse("2020-01-15 06:00:00", formatter))
                .setAmount(1.5f);
        YahooDividend div_15_jun = new YahooDividend()
                .setExDate(LocalDateTime.parse("2020-06-15 06:00:00", formatter))
                .setAmount(2.5f);
        YahooDividend div_15_sep = new YahooDividend()
                .setExDate(LocalDateTime.parse("2020-09-15 06:00:00", formatter))
                .setAmount(1.0f);
        YahooDividend div_15_dec = new YahooDividend()
                .setExDate(LocalDateTime.parse("2020-12-15 06:00:00", formatter))
                .setAmount(1.2f);

        when(yahooApiClient.detDividends("APP", firstTransactionDate))
                .thenReturn(List.of(div_15_jan, div_15_jun, div_15_sep, div_15_dec));

        dividendService = new DividendService(dividendRepository, positionRepository, transactionRepository, yahooApiClient);


        List<DividendResponse> dividendEntities = dividendService.getDividendsBySymbolAndOwner("APP", "Olga");
        assertEquals(dividendEntities.size(), 4);

        List<DividendResponse> sortedDividends = dividendEntities.stream()
                .sorted(Comparator.comparing(DividendResponse::getExDate))
                .collect(Collectors.toList());

        DividendResponse div_15_jan_resp = sortedDividends.get(0);
        assertEquals(10,div_15_jan_resp.getShareAmount());
        assertEquals(15.0f,div_15_jan_resp.getDollarBruttoAmount());
        assertEquals(LocalDateTime.parse("2020-01-15 06:00:00", formatter),div_15_jan_resp.getExDate() );

        DividendResponse div_15_jun_resp = sortedDividends.get(1);
        assertEquals(13, div_15_jun_resp.getShareAmount());
        assertEquals(32.5f, div_15_jun_resp.getDollarBruttoAmount());
        assertEquals(LocalDateTime.parse("2020-06-15 06:00:00", formatter),div_15_jun_resp.getExDate());

        DividendResponse div_15_sep_resp = sortedDividends.get(2);
        assertEquals(13, div_15_sep_resp.getShareAmount());
        assertEquals(13.0f, div_15_sep_resp.getDollarBruttoAmount());
        assertEquals(LocalDateTime.parse("2020-09-15 06:00:00", formatter), div_15_sep_resp.getExDate());

        DividendResponse div_15_dec_resp = sortedDividends.get(3);
        assertEquals(8, div_15_dec_resp.getShareAmount());
        assertEquals(9.6f, div_15_dec_resp.getDollarBruttoAmount());
        assertEquals(LocalDateTime.parse("2020-12-15 06:00:00", formatter), div_15_dec_resp.getExDate());


    }
}