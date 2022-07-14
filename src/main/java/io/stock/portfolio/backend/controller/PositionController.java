package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.PortfolioResponse;
import io.stock.portfolio.backend.controller.model.PositionsResponse;
import io.stock.portfolio.backend.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/v1/positions", produces = {"application/json"})
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/username/{owner}")
    @ResponseStatus(HttpStatus.OK)
    public PositionsResponse getProfileByUsername(@PathVariable String owner, @RequestParam(value = "stockNameLike", required = false) String stockNameLike, Pageable pageable) {
        return positionService.getPositionsByOwner(owner, stockNameLike, pageable);
    }

    @GetMapping("/username/{owner}/sold")
    @ResponseStatus(HttpStatus.OK)
    public PositionsResponse getSoldPositions(@PathVariable String owner, Pageable pageable) {
        return positionService.getSoldPositionsByOwner(owner, pageable);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Map<String, PortfolioResponse> getPortfolio() {
        return positionService.getPortfolio();
    }

}
