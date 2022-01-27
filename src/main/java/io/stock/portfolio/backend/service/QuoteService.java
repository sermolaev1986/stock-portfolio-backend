package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.yahoo.YahooApiClient;
import io.stock.portfolio.backend.client.yahoo.YahooQuote;
import io.stock.portfolio.backend.controller.model.QuoteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final YahooApiClient yahooApiClient;

    public Optional<QuoteDTO> getQuote(String symbol) {
        var maybeQuote = yahooApiClient.getQuote(symbol);
        return maybeQuote.map(quote -> convertQuote(symbol, quote));
    }

    public List<QuoteDTO> getQuotes(List<String> symbols) {
        return symbols.stream()
                .map(this::getQuote)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private QuoteDTO convertQuote(String symbol, YahooQuote yahooQuote) {
        return new QuoteDTO()
                .setPrice(yahooQuote.getPrice())
                .setSymbol(symbol);
    }
}
