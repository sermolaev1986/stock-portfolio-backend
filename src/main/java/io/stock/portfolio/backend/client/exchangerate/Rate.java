package io.stock.portfolio.backend.client.exchangerate;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;


public class Rate {

    private BigDecimal USD;

    @JsonAlias("USD")
    public BigDecimal getUSD() {
        return USD;
    }

    @JsonAlias("USD")
    public void setUSD(BigDecimal USD) {
        this.USD = USD;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "USD=" + USD +
                '}';
    }
}
