package io.stock.portfolio.backend.config;

import io.stock.portfolio.backend.service.DividendService;
import io.stock.portfolio.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class OnStartup {
    private final TransactionService transactionService;
    private final DividendService dividendService;

    @PostConstruct
    public void updatePositions(){
        dividendService.updateDividends(); // Should be called before update positions, because there split transaction will be saved
        transactionService.updatePositions();
    }
}
