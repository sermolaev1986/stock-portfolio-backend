package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.PositionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PositionRepository extends PagingAndSortingRepository<PositionEntity, Long> {

    @Query("SELECT position from PositionEntity position " +
            "where position.owner=:owner AND position.stockCount <> 0")
    List<PositionEntity> findByOwner(String owner, Pageable pageable);

    List<PositionEntity> findByOwnerAndStockCount(String owner, BigDecimal stockCount, Pageable pageable);

    Optional<PositionEntity> findBySymbolAndOwner(String symbol, String owner);
}
