package io.stock.portfolio.backend.client.divvydairy;

import lombok.Data;

import java.util.List;

@Data
public class DivvyDairyResponse {
    private List<DivvyDairyDividend> dividends;
}
