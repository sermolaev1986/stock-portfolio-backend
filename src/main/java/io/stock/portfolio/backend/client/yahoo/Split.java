package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.time.Instant;

@Data
public class Split {

    private Instant date;
    private Integer numerator;
    private Integer denominator;

    public Float getMultiplier() {
        return  1.0F * numerator / denominator;
    }
}
