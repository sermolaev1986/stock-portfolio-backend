package io.stock.portfolio.backend.database.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "owner")
    private String owner;
    @Column(name = "argument")
    private BigDecimal argument;
    @Convert(converter = OperatorConverter.class)
    @Column(name = "operator")
    private Operator operator;
    @Column(name = "price")
    private BigDecimal price;


    public BigDecimal getTotalPrice() {
        if (isSplit()) {
            return BigDecimal.ZERO;
        } else {
            return price.multiply(argument);
        }
    }

    public boolean isSplit() {
        return operator == Operator.MULTIPLY;
    }
}
