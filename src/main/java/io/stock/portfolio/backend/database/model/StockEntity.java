package io.stock.portfolio.backend.database.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "stock")
public class StockEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "euSymbol")
    private String euSymbol;

    @Column(name = "stockName")
    private String name;

    @Column(name = "usSymbol")
    private String usSymbol;

    @Column(name = "type")
    private String type;

}
