package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.QuoteDTO;
import io.stock.portfolio.backend.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/quotes", produces = {"application/json"})
@RequiredArgsConstructor
public class QuotesController {

    private final QuoteService quoteService;

    @GetMapping("/{symbol}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<QuoteDTO> getQuote(@PathVariable String symbol) {
        return quoteService.getQuote(symbol);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuoteDTO> getQuotes(@RequestParam("symbols") List<String> symbols) {
        return quoteService.getQuotes(symbols);
    }

}
