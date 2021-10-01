package io.stock.portfolio.backend.client.exchangerate;

import com.fasterxml.jackson.annotation.JsonAlias;


public class Rate {

    private Float USD;

    @JsonAlias("USD")
    public Float getUSD() {
        return USD;
    }

    @JsonAlias("USD")
    public void setUSD(Float USD) {
        this.USD = USD;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "USD=" + USD +
                '}';
    }
}
