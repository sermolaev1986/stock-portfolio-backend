package io.stock.portfolio.backend.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PositionsResponse {
    private long totalPositions;
    private List<PositionResponse> positions;
}
