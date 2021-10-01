package io.stock.portfolio.backend.client.exchangerate;

import io.stock.portfolio.backend.client.yahoo.Dividend;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class Response {
    private String base;
    private Rate rates;

}
