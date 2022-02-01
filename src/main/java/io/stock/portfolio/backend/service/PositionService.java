package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.PortfolioResponse;
import io.stock.portfolio.backend.controller.model.PositionResponse;
import io.stock.portfolio.backend.database.model.DividendEntity;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.model.StockEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import io.stock.portfolio.backend.database.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
public class PositionService {

    private final PositionRepository positionRepository;
    private final StockRepository stockRepository;
    private final DividendService dividendService;
    private final TransactionService transactionService;

    public Map<String, PortfolioResponse> getPortfolio() {
        var positions = positionRepository.findAll(Pageable.ofSize((int) positionRepository.count()))
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.groupingBy(PositionResponse::getOwner));

        var investmentsPerOwner = transactionService.getInvestmentsPerOwner();
        var portfolio = new HashMap<String, PortfolioResponse>();
        for (Map.Entry<String, List<PositionResponse>> entry : positions.entrySet()) {
            portfolio.put(entry.getKey(), new PortfolioResponse()
                    .setPositions(entry.getValue())
                    .setInvestments(investmentsPerOwner.get(entry.getKey()))
            );
        }

        return portfolio;
    }

    public List<PositionResponse> getPositionsByOwner(String owner, int page, int pageSize) {
        return doGetPositionsByOwner(owner,
                () -> positionRepository.findByOwner(owner, PageRequest.of(page, pageSize)));
    }

    public List<PositionResponse> getSoldPositionsByOwner(String owner, int page, int pageSize) {
        return doGetPositionsByOwner(owner,
                () -> positionRepository.findByOwnerAndStockCount(owner, BigDecimal.ZERO, PageRequest.of(page, pageSize)));
    }

    private List<PositionResponse> doGetPositionsByOwner(String owner, Supplier<List<PositionEntity>> positionSupplier) {
        var dividends = dividendService.getDividendsByOwner(owner);
        var investments = transactionService.getInvestmentsPerSymbol(owner);

        return positionSupplier.get()
                .stream()
                .map(entity -> convertToResponse(entity, investments.get(entity.getSymbol())))
                .map(positionResponse -> enrichWithDividends(positionResponse, dividends.get(positionResponse.getSymbol())))
                .collect(Collectors.toList());
    }

    public void postPosition(PositionResponse positionResponse) {
        positionRepository.save(convertToEntity(positionResponse));
    }

    private PositionResponse convertToResponse(PositionEntity position, BigDecimal investments) {
        return convertToResponse(position)
                .setInvestments(investments);
    }

    private PositionResponse convertToResponse(PositionEntity position) {
        var stock = stockRepository.findByEuSymbol(position.getSymbol());
        return new PositionResponse()
                .setOwner(position.getOwner())
                .setSymbol(position.getSymbol())
                .setUsSymbol(stock.map(StockEntity::getUsSymbol).orElse(null))
                .setName(stock.map(StockEntity::getName).orElse(null))
                .setStockCount(position.getStockCount())
                .setBuyDate(position.getBuyDate())
                .setBroker(position.getBroker());
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
