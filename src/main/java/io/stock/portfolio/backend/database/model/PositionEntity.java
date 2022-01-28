package io.stock.portfolio.backend.database.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "position")
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "stockName")
    private String name;

    @Column(name = "stockCount")
    private BigDecimal stockCount;

    @Column(name = "owner")
    private String owner;

    @Column(name = "buyDate")
    private LocalDateTime buyDate;

    @Column(name = "broker")
    private String broker;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionEntity)) return false;
        PositionEntity position = (PositionEntity) o;
        return Objects.equals(id, position.id) && symbol.equals(position.symbol) && owner.equals(position.owner) && buyDate.equals(position.buyDate) && broker.equals(position.broker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, owner, buyDate, broker);
    }
}
