package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.PortfolioResponse;
import io.stock.portfolio.backend.controller.model.PositionResponse;
import io.stock.portfolio.backend.database.model.DividendEntity;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final DividendService dividendService;
    private final TransactionService transactionService;

    public Map<String, PortfolioResponse> getPortfolio() {
        var investmentsPerOwner = transactionService.getInvestmentsPerOwner();

        var positions = positionRepository.findAll()
                .stream()
                .map(entity -> convertToResponse(entity, investmentsPerOwner))
                .collect(Collectors.groupingBy(PositionResponse::getOwner));

        var portfolio = new HashMap<String, PortfolioResponse>();
        for (Map.Entry<String, List<PositionResponse>> entry : positions.entrySet()) {
            portfolio.put(entry.getKey(), new PortfolioResponse()
                    .setPositions(entry.getValue())
                    .setInvestments(investmentsPerOwner.get(entry.getKey()))
            );
        }

        return portfolio;
    }

    public List<PositionResponse> getAllPositionsByOwner(String owner) {
        var dividends = dividendService.getDividendsByOwner(owner);

        return positionRepository.findByOwner(owner)
                .stream()
                .map(this::convertToResponse)
                .map(positionResponse -> enrichWithDividends(positionResponse, dividends.get(positionResponse.getSymbol())))
                .collect(Collectors.toList());
    }

    public void postPosition(PositionResponse positionResponse) {
        positionRepository.save(convertToEntity(positionResponse));
    }

    private PositionResponse convertToResponse(PositionEntity entity) {
        return convertToResponse(entity, new HashMap<>());
    }

    private PositionResponse convertToResponse(PositionEntity entity, Map<String, BigDecimal> investmentsPerOwner) {
        return new PositionResponse()
                .setOwner(entity.getOwner())
                .setSymbol(entity.getSymbol())
                .setStockCount(entity.getStockCount())

                .setBroker(entity.getBroker());
    }

    private PositionResponse enrichWithDividends(PositionResponse positionResponse, List<DividendEntity> dividends) {
        if (dividends == null) {
            dividends = Collections.emptyList();
        }
        positionResponse.setDividends(dividendService.getTotalDividendsEuroNetto(positionResponse.getSymbol(),
                positionResponse.getOwner(), dividends));

        return positionResponse;

    }

    private PositionEntity convertToEntity(PositionResponse positionResponse) {
        return new PositionEntity()
                .setOwner(positionResponse.getOwner())
                .setSymbol(positionResponse.getSymbol())
                .setStockCount(positionResponse.getStockCount())
                .setBroker(positionResponse.getBroker());
    }
}
