package io.stock.portfolio.backend.database.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "stockCount")
    private Integer stockCount;

    @Column(name = "owner")
    private String owner;

    @Column(name = "buyDate")
    private LocalDateTime buyDate;

    @Column(name = "broker")
    private String broker;

}
