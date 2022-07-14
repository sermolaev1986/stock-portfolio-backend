package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.PortfolioResponse;
import io.stock.portfolio.backend.controller.model.PositionResponse;
import io.stock.portfolio.backend.controller.model.PositionsResponse;
import io.stock.portfolio.backend.database.model.DividendEntity;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionService {

    private final PositionRepository positionRepository;
    private final DividendService dividendService;
    private final TransactionService transactionService;

    public Map<String, PortfolioResponse> getPortfolio() {
        var positions = positionRepository.findAll(Pageable.ofSize((int) positionRepository.count()))
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.groupingBy(PositionResponse::getOwner));

        var portfolio = new HashMap<String, PortfolioResponse>();
        for (Map.Entry<String, List<PositionResponse>> entry : positions.entrySet()) {
            BigDecimal investmentsPerPosition = entry.getValue().stream()
                    .map(PositionResponse::getInvestments)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            portfolio.put(entry.getKey(), new PortfolioResponse()
                    .setPositions(entry.getValue())
                    .setInvestments(investmentsPerPosition)
            );
        }

        return portfolio;
    }

    public PositionsResponse getPositionsByOwner(String owner, String stockNameLike, Pageable pageable) {
        return doGetPositionsByOwner(owner,
                () -> {
                    if (stockNameLike != null && !stockNameLike.isBlank()) {
                        return positionRepository.findByOwnerAndStockNameContainingIgnoreCase(owner, stockNameLike, pageable);
                    } else {
                        return positionRepository.findByOwner(owner, pageable);
                    }
                }
        );
    }

    public PositionsResponse getSoldPositionsByOwner(String owner, Pageable pageable) {
        return doGetPositionsByOwner(owner, () -> positionRepository.findByOwnerAndStockCount(owner, BigDecimal.ZERO, pageable));
    }

    private PositionsResponse doGetPositionsByOwner(String owner, Supplier<Page<PositionEntity>> positionSupplier) {

        var dividends = dividendService.getDividendsByOwner(owner);
        var investments = transactionService.getInvestmentsPerSymbol(owner);

        log.info("--------calling repository");
        var positionsPage = positionSupplier.get();
        log.info("--------called repository");

        var positions = positionsPage.getContent()
                .stream()
                .map(entity -> convertToResponse(entity, investments.get(entity.getSymbol())))
                .map(positionResponse -> enrichWithDividends(positionResponse, dividends.get(positionResponse.getSymbol())))
                .collect(Collectors.toList());
        return new PositionsResponse()
                .setTotalPositions(positionsPage.getTotalElements())
                .setPositions(positions);
    }

    private PositionResponse convertToResponse(PositionEntity position, BigDecimal investments) {
        return convertToResponse(position)
                .setInvestments(investments);
    }

    private PositionResponse convertToResponse(PositionEntity position) {
        return new PositionResponse()
                .setOwner(position.getOwner())
                .setSymbol(position.getSymbol())
                .setUsSymbol(position.getStock().getUsSymbol())
                .setName(position.getStock().getName())
                .setType(position.getStock().getType())
                .setStockCount(position.getStockCount())
                .setBuyDate(position.getBuyDate())
                .setBroker(position.getBroker())
                .setInvestments(position.getTotalInvestments());
    }

    private PositionResponse enrichWithDividends(PositionResponse positionResponse, List<DividendEntity> dividends) {
        if (dividends == null) {
            dividends = Collections.emptyList();
        }
        positionResponse.setDividends(dividendService.getTotalDividendsEuroNetto(positionResponse.getSymbol(),
                positionResponse.getOwner(), dividends));

        return positionResponse;

    }
}
