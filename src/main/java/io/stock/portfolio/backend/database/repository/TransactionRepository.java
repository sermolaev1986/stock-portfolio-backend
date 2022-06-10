package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.Operator;
import io.stock.portfolio.backend.database.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findBySymbolAndOwnerAndDateAndOperatorAndArgument(String symbol, String owner, LocalDateTime date, Operator operator, BigDecimal argument);
    List<TransactionEntity> findBySymbolAndOwnerOrderByDateAsc(String symbol, String owner);

    List<TransactionEntity> findByOwnerOrderByDateAsc(String owner);
}
