package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Meta {
    private String symbol;
    private BigDecimal regularMarketPrice;
}
