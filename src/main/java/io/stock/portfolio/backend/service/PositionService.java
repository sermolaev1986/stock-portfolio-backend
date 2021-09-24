package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.PositionDTO;
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

    public List<PositionDTO> getAllPositions() {
        return positionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PositionDTO> getAllPositionsByOwner(String owner) {
        return positionRepository.findByOwner(owner)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void postPosition(PositionDTO positionDTO) {
        positionRepository.save(convertToEntity(positionDTO));
    }


    private PositionDTO convertToResponse(PositionEntity entity) {
        return new PositionDTO()
                .setOwner(entity.getOwner())
                .setSymbol(entity.getSymbol())
                .setBuyPrice(entity.getBuyPrice())
                .setStockCount(entity.getStockCount())
                .setBuyDate(entity.getBuyDate())
                .setBroker(entity.getBroker());
    }

    private PositionEntity convertToEntity(PositionDTO positionDTO) {
        return new PositionEntity()
                .setOwner(positionDTO.getOwner())
                .setSymbol(positionDTO.getSymbol())
                .setBuyPrice(positionDTO.getBuyPrice())
                .setStockCount(positionDTO.getStockCount())
                .setBuyDate(positionDTO.getBuyDate())
                .setBroker(positionDTO.getBroker());
    }
}
