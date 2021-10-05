package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Data
public class Split {

    private Instant date;
    private BigDecimal numerator;
    private BigDecimal denominator;

    public BigDecimal getMultiplier() {
        return numerator.divide(denominator, RoundingMode.HALF_DOWN);
    }
}
