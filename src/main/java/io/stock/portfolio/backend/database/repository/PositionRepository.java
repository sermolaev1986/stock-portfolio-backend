package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    Set<PositionEntity> findByOwner(String owner);
    Set<PositionEntity> findBySymbolAndOwner(String symbol, String owner);
}
