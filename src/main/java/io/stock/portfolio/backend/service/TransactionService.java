package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.TransactionDTO;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.model.TransactionEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import io.stock.portfolio.backend.database.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;

    public Map<String, BigDecimal> getInvestmentsPerOwner() {
        final Map<String, List<TransactionEntity>> transactionsByOwner = transactionRepository.findAll().stream()
                .collect(Collectors.groupingBy(TransactionEntity::getOwner));
        var map = new HashMap<String, BigDecimal>();
        for (Map.Entry<String, List<TransactionEntity>> entry : transactionsByOwner.entrySet()) {
            map.put(entry.getKey(), countTransactions(entry.getValue()));
        }

        return map;
    }

    public Map<String, BigDecimal> getInvestmentsPerSymbol(String owner) {
        final Map<String, List<TransactionEntity>> transactionsByOwner = transactionRepository.findByOwnerOrderByDateAsc(owner).stream()
                .collect(Collectors.groupingBy(TransactionEntity::getSymbol));
        var map = new HashMap<String, BigDecimal>();

        for (Map.Entry<String, List<TransactionEntity>> entry : transactionsByOwner.entrySet()) {
            BigDecimal pricePerPosition = BigDecimal.ZERO;

            for (TransactionEntity transactionEntity : entry.getValue()) {
                if (!transactionEntity.isSplit()) {
                    pricePerPosition = transactionEntity.getOperator().calculateTotalPrice(pricePerPosition, transactionEntity.getTotalPrice());
                }
            }
            map.put(entry.getKey(), pricePerPosition);
        }

        return map;
    }

    private BigDecimal countTransactions(List<TransactionEntity> transactions) {
        final Map<String, List<TransactionEntity>> transactionsBySymbol = transactions.stream()
                .collect(Collectors.groupingBy(TransactionEntity::getSymbol));

        var totalPrice = BigDecimal.ZERO;

        for (Map.Entry<String, List<TransactionEntity>> entry : transactionsBySymbol.entrySet()) {
            var transactionEntityList = entry.getValue();
            transactionEntityList.sort(Comparator.comparing(TransactionEntity::getDate));

            BigDecimal pricePerPosition = BigDecimal.ZERO;

            for (TransactionEntity transactionEntity : transactionEntityList) {
                if (!transactionEntity.isSplit()) {
                    pricePerPosition = transactionEntity.getOperator().calculateTotalPrice(pricePerPosition, transactionEntity.getTotalPrice());
                }
            }

            totalPrice = totalPrice.add(pricePerPosition);
        }

        return totalPrice;
    }

    public List<TransactionDTO> getTransactionsByOwner(String owner) {
        return transactionRepository.findByOwnerOrderByDateAsc(owner)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = convertToEntity(transactionDTO);
        transactionRepository.save(transactionEntity);

        Optional<PositionEntity> maybePosition = positionRepository.findBySymbolAndOwner(transactionDTO.getSymbol(), transactionDTO.getOwner());
        if (maybePosition.isPresent()) {
            PositionEntity position = maybePosition.get();
            BigDecimal stockCountBefore = position.getStockCount();

            BigDecimal stockCountAfter = transactionEntity.getOperator().calculateAmountOfShares(stockCountBefore, transactionEntity.getArgument());
            position.setStockCount(stockCountAfter); // Should be updated TODO Check
        } else {
            BigDecimal stockCount = transactionEntity.getOperator().calculateAmountOfShares(BigDecimal.ZERO, transactionEntity.getArgument());

            PositionEntity position = new PositionEntity()
                    .setOwner(transactionDTO.getOwner())
                    .setSymbol(transactionDTO.getSymbol())
                    .setStockCount(stockCount)
                    .setBuyDate(transactionDTO.getDate())
                    .setBroker(transactionDTO.getBroker());

            positionRepository.save(position);
        }


    }

    private TransactionDTO convertToResponse(TransactionEntity entity) {
        return new TransactionDTO()
                .setSymbol(entity.getSymbol())
                .setDate(entity.getDate())
                .setArgument(entity.getArgument())
                .setOperator(entity.getOperator());
    }

    private TransactionEntity convertToEntity(TransactionDTO dto) {
        return new TransactionEntity()
                .setSymbol(dto.getSymbol())
                .setDate(dto.getDate())
                .setArgument(dto.getArgument())
                .setOwner(dto.getOwner())
                .setOperator(dto.getOperator());
    }
}
