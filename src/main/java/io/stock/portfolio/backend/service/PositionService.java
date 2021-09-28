package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.PositionResponse;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final DividendService dividendService;

    public List<PositionResponse> getAllPositions() {
        return positionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PositionResponse> getAllPositionsByOwner(String owner) {
        return positionRepository.findByOwner(owner)
                .stream()
                .map(this::convertToResponse)
                .map(this::enrichWithDividends)
                .collect(Collectors.toList());
    }

    public void postPosition(PositionResponse positionResponse) {
        positionRepository.save(convertToEntity(positionResponse));
    }


    private PositionResponse convertToResponse(PositionEntity entity) {

        return new PositionResponse()
                .setOwner(entity.getOwner())
                .setSymbol(entity.getSymbol())
                .setStockCount(entity.getStockCount())
                .setBuyDate(entity.getBuyDate())
                .setBroker(entity.getBroker());
    }

    private PositionResponse enrichWithDividends(PositionResponse positionResponse) {
        return positionResponse
                .setDividends(dividendService.getTotalDividendsEuroNetto(positionResponse.getSymbol(), positionResponse.getOwner()));
    }

    private PositionEntity convertToEntity(PositionResponse positionResponse) {
        return new PositionEntity()
                .setOwner(positionResponse.getOwner())
                .setSymbol(positionResponse.getSymbol())
                .setStockCount(positionResponse.getStockCount())
                .setBuyDate(positionResponse.getBuyDate())
                .setBroker(positionResponse.getBroker());
    }
}
