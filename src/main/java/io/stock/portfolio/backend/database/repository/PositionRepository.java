package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.PositionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PositionRepository extends PagingAndSortingRepository<PositionEntity, Long> {

    List<PositionEntity> findByOwner(String owner, Pageable pageable);

    List<PositionEntity> findByOwnerAndStockCount(String owner, BigDecimal stockCount, Pageable pageable);

    Optional<PositionEntity> findBySymbolAndOwner(String symbol, String owner);
}
