package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.util.Map;

@Data
public class Events {
    private Map<String, Dividend> dividends;
}
