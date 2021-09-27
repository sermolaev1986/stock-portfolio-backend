package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.util.List;
@Data
public class Chart {
    private List<Result> result;
}
