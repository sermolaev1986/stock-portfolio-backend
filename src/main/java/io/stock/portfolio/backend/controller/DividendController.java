package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.DividendResponse;
import io.stock.portfolio.backend.service.DividendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/dividends", produces = {"application/json"})
@RequiredArgsConstructor
public class DividendController {

    private final DividendService dividendService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<DividendResponse> getProfileByUsername(@RequestParam String symbol, @RequestParam String owner) {

        return dividendService.getDividendsBySymbolAndOwner(symbol, owner);
    }

}
