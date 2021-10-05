package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class YahooSplit {
    private LocalDateTime date;
    private BigDecimal multiplier;
}
