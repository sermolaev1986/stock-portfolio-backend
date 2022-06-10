package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class YahooDividendsAndSplits {
    private List<YahooDividend> dividends = new ArrayList<>();
    private List<YahooSplit> splits = new ArrayList<>();
}
