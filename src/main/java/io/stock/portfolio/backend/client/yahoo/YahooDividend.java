package io.stock.portfolio.backend.client.yahoo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class YahooDividend {
    private LocalDateTime exDate;
    private Float amount;
}
