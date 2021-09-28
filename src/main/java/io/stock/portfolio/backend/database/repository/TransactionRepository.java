package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findBySymbolAndOwnerOrderByDateAsc(String symbol, String owner);
    List<TransactionEntity> findByOwnerOrderByDateAsc( String owner);
}
