package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.yahoo.YahooApiClient;
import io.stock.portfolio.backend.client.yahoo.YahooDividendsAndSplits;
import io.stock.portfolio.backend.client.yahoo.YahooSplit;
import io.stock.portfolio.backend.controller.model.SymbolOwner;
import io.stock.portfolio.backend.controller.model.TransactionDTO;
import io.stock.portfolio.backend.database.model.Operator;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.model.TransactionEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import io.stock.portfolio.backend.database.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;
    private final YahooApiClient yahooApiClient;

    public Map<String, BigDecimal> getInvestmentsPerOwner() {
        final Map<String, List<TransactionEntity>> transactionsByOwner = transactionRepository.findAll()
                .stream()
                .collect(groupingBy(TransactionEntity::getOwner));
        var map = new HashMap<String, BigDecimal>();
        for (Map.Entry<String, List<TransactionEntity>> entry : transactionsByOwner.entrySet()) {
            map.put(entry.getKey(), countTransactions(entry.getValue()));
        }

        return map;
    }

    public Map<String, BigDecimal> getInvestmentsPerSymbol(String owner) {
        final Map<String, List<TransactionEntity>> transactionsByOwner = transactionRepository.findByOwnerOrderByDateAsc(owner).stream().collect(groupingBy(TransactionEntity::getSymbol));
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
        final Map<String, List<TransactionEntity>> transactionsBySymbol = transactions.stream().collect(groupingBy(TransactionEntity::getSymbol));

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
        return transactionRepository.findByOwnerOrderByDateAsc(owner).stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void saveTransactions(List<TransactionDTO> transactionDTOs) {
        List<TransactionEntity> transactionEntities = transactionDTOs.stream().map(this::convertToEntity).collect(Collectors.toList());
        transactionRepository.saveAll(transactionEntities);

        saveOrUpdatePositionsCorrespondingToTransactions(transactionEntities);
    }

    // Only use on App startup
    @Transactional
    public void updatePositions() {
        saveOrUpdatePositionsCorrespondingToTransactions(transactionRepository.findAll());
    }

    private void saveOrUpdatePositionsCorrespondingToTransactions(List<TransactionEntity> transactionEntities) {
        Map<SymbolOwner, List<TransactionEntity>> transactionsBySymbolAndOwner = transactionEntities
                .stream()
                .collect(groupingBy(tr -> new SymbolOwner(tr.getSymbol(), tr.getOwner())));

        List<PositionEntity> positionsToSave = new ArrayList<>();

        for (Map.Entry<SymbolOwner, List<TransactionEntity>> entry : transactionsBySymbolAndOwner.entrySet()) {

            List<TransactionEntity> orderedTransactions = entry.getValue();
            orderedTransactions.sort(Comparator.comparing(TransactionEntity::getDate));

            TransactionEntity firstTransaction = orderedTransactions.get(0);

            var maybeResponse = yahooApiClient.getDividendsAndSplits(entry.getKey().getSymbol(), firstTransaction.getDate());

            if (maybeResponse.isPresent()) {
                Set<TransactionEntity> splitTransactions = maybeResponse.get().getSplits().stream().map(yahooSplit ->
                        new TransactionEntity()
                                .setSymbol(entry.getKey().getSymbol())
                                .setOwner(entry.getKey().getOwner())
                                .setDate(yahooSplit.getDate())
                                .setOperator(Operator.MULTIPLY)
                                //TODO will not work for reverse splits
                                .setArgument(yahooSplit.getMultiplier())
                ).collect(Collectors.toSet());
                orderedTransactions.addAll(splitTransactions);
                orderedTransactions.sort(Comparator.comparing(TransactionEntity::getDate));
            }

            // Position status after the first Transaction
            PositionEntity position = new PositionEntity()
                    .setOwner(firstTransaction.getOwner())
                    .setSymbol(firstTransaction.getSymbol())
                    .setStockCount(firstTransaction.getOperator().calculateAmountOfShares(BigDecimal.ZERO, firstTransaction.getArgument()))
                    .setBuyDate(firstTransaction.getDate());

            // Starting from 1 because 1st event is a first transaction which was used to create initial Position
            for (int i = 1; i < orderedTransactions.size(); i++) {
                TransactionEntity transaction = orderedTransactions.get(i);// It can be normal transaction or split transaction
                position.setStockCount(transaction.getOperator().calculateAmountOfShares(position.getStockCount(), transaction.getArgument()));
            }
            positionsToSave.add(position);
        }
        positionRepository.saveAll(positionsToSave);

    }

    private TransactionDTO convertToResponse(TransactionEntity entity) {
        return new TransactionDTO().setSymbol(entity.getSymbol()).setDate(entity.getDate()).setArgument(entity.getArgument()).setOperator(entity.getOperator());
    }

    private TransactionEntity convertToEntity(TransactionDTO dto) {
        return new TransactionEntity().setSymbol(dto.getSymbol()).setDate(dto.getDate()).setArgument(dto.getArgument()).setOwner(dto.getOwner()).setOperator(dto.getOperator());
    }
}
