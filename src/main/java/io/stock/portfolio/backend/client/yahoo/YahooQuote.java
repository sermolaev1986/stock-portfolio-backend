package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class YahooQuote {
    private BigDecimal price;
}
