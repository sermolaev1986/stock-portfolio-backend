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
@Table(name = "dividend")
public class DividendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "amountPerShare")
    private BigDecimal amountPerShare;
    @Column(name = "exchangeRate")
    private BigDecimal exchangeRate; // How much does 1 EUR cost
    @Column(name = "exDate")
    private LocalDateTime exDate;
    @Column(name = "shareAmount")
    private Integer shareAmount;
    @Column(name = "owner")
    private String owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DividendEntity)) return false;
        DividendEntity that = (DividendEntity) o;
        return Objects.equals(id, that.id) && symbol.equals(that.symbol) && exDate.equals(that.exDate) && owner.equals(that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, exDate, owner);
    }
}
