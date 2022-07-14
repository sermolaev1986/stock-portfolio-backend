package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.PositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PositionRepository extends PagingAndSortingRepository<PositionEntity, Long> {

    Page<PositionEntity> findByOwner(String owner, Pageable pageable);

    Page<PositionEntity> findByOwnerAndStockNameContainingIgnoreCase(String owner, String stockName, Pageable pageable);

    Page<PositionEntity> findByOwnerAndStockCount(String owner, BigDecimal stockCount, Pageable pageable);

    Optional<PositionEntity> findByStockEuSymbolAndOwner(String symbol, String owner);
}
