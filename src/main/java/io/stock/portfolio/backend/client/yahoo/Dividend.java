package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Dividend {
    private BigDecimal amount;
    private long date;
}
