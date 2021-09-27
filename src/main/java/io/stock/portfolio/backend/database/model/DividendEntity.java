package io.stock.portfolio.backend.database.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "dollarBruttoAmount")
    private Float dollarBruttoAmount;
    @Column(name = "euroBruttoAmount")
    private Float euroBruttoAmount;
    @Column(name = "exDate")
    private LocalDateTime exDate;
}
