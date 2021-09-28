package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.controller.model.TransactionDTO;
import io.stock.portfolio.backend.database.model.PositionEntity;
import io.stock.portfolio.backend.database.model.TransactionEntity;
import io.stock.portfolio.backend.database.repository.PositionRepository;
import io.stock.portfolio.backend.database.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;

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
            Integer stockCountBefore = position.getStockCount();

            Integer stockCountAfter = transactionEntity.getOperator().calculate(stockCountBefore, transactionEntity.getArgument());
            position.setStockCount(stockCountAfter); // Should be updated TODO Check
        } else {
            Integer stockCount = transactionEntity.getOperator().calculate(0, transactionEntity.getArgument());

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
                .setOperator(dto.getOperator());
    }
}
