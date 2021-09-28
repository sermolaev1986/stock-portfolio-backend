package io.stock.portfolio.backend.database.repository;

import io.stock.portfolio.backend.database.model.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    List<DividendEntity> findBySymbolAndOwner(String symbol, String owner);
}
