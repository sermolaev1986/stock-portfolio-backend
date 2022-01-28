package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, Long> {

    Optional<StockEntity> findByEuSymbol(String euSymbol);
}
