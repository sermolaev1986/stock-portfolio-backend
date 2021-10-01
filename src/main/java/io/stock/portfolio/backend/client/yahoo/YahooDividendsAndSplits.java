package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class YahooDividendsAndSplits {
    private List<YahooDividend> dividends;
    private List<YahooSplit> splits;
}
