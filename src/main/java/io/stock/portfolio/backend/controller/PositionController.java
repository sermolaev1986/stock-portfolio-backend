package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.PortfolioResponse;
import io.stock.portfolio.backend.controller.model.PositionResponse;
import io.stock.portfolio.backend.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/positions", produces = {"application/json"})
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/username/{owner}")
    @ResponseStatus(HttpStatus.OK)
    public List<PositionResponse> getProfileByUsername(@PathVariable String owner, Pageable pageable) {
        return positionService.getPositionsByOwner(owner, pageable);
    }

    @GetMapping("/username/{owner}/sold")
    @ResponseStatus(HttpStatus.OK)
    public List<PositionResponse> getSoldPositions(@PathVariable String owner, Pageable pageable) {
        return positionService.getSoldPositionsByOwner(owner, pageable);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Map<String, PortfolioResponse> getPortfolio() {
        return positionService.getPortfolio();
    }

}
