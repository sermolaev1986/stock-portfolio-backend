package io.stock.portfolio.backend.client.divvydairy;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DivvyDairyDividend {
    private Date exDate;
    private Date payDate;
    private String name;
    private String symbol;
    private String isin;
    private String currency;
    private BigDecimal amount;
    private BigDecimal quantity;
    private BigDecimal exchangeRate;
}
