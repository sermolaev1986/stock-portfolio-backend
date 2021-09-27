package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

@Data
public class Result {
    private Meta meta;
    private Events events;
}
