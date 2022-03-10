package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    List<DividendEntity> findBySymbolAndOwner(String symbol, String owner);
    Optional<DividendEntity> findFirstBySymbolAndOwnerOrderByExDateDesc(String symbol, String owner);
    List<DividendEntity> findByOwner(String owner);
}
