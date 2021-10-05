package io.stock.portfolio.backend.client.yahoo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class YahooApiClient {

    private final RestTemplate restTemplate;

    public Optional<YahooDividendsAndSplits> getDividendsAndSplits(String symbol, LocalDateTime lastDividendDate) {
        long from = lastDividendDate.atZone(ZoneId.systemDefault()).toEpochSecond();
        long to = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        String yahooUrl = String.format("https://query1.finance.yahoo.com/v8/finance/chart/%s.BE", symbol);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(yahooUrl)
                .queryParam("symbol", symbol)
                .queryParam("period1", from)
                .queryParam("period2", to)
                .queryParam("interval", "1mo")
                .queryParam("includePrePost", true)
                .queryParam("events", "div,split");


        try {
            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(new LinkedMultiValueMap<>()),
                    Response.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return Optional.of(new YahooDividendsAndSplits()
                        .setDividends(convertToYahooDividend(Objects.requireNonNull(responseEntity.getBody())))
                        .setSplits(convertToYahooSplit(Objects.requireNonNull(responseEntity.getBody()))));
            }
        } catch (HttpClientErrorException exception) {
            log.error("error getting dividends for symbol {}: ", symbol, exception);
            return Optional.empty();
        }

        return Optional.empty();
    }

    private List<YahooSplit> convertToYahooSplit(Response response) {
        return response.getChart().getResult().stream()
                .map(Result::getEvents)
                .filter(Objects::nonNull)
                .map(Events::getSplits)
                .filter(Objects::nonNull)
                .flatMap(div -> div.entrySet().stream())
                .map(Map.Entry::getValue)
                .map(split ->
                        new YahooSplit()
                                .setDate(LocalDateTime.ofInstant(split.getDate(), ZoneOffset.UTC))
                                .setMultiplier(split.getMultiplier())
                )
                .collect(Collectors.toList());
    }

    private List<YahooDividend> convertToYahooDividend(Response response) {
        final List<Result> results = response.getChart().getResult();
        return results.stream()
                .map(Result::getEvents)
                .filter(Objects::nonNull)
                .map(Events::getDividends)
                .filter(Objects::nonNull)
                .flatMap(div -> div.entrySet().stream())
                .map(Map.Entry::getValue)
                .map(div -> {
                    YahooDividend yahooDividend = new YahooDividend();

                    Instant instant = Instant.ofEpochSecond(div.getDate());
                    LocalDateTime exDate = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                    yahooDividend.setExDate(exDate);

                    yahooDividend.setAmount(adjustDividendToSplits(results, instant, div.getAmount()));

                    return yahooDividend;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal adjustDividendToSplits(List<Result> results, Instant exDate, BigDecimal amount) {
        return results.stream()
                .map(Result::getEvents)
                .filter(Objects::nonNull)
                .map(Events::getSplits)
                .filter(Objects::nonNull)
                .flatMap(div -> div.entrySet().stream())
                .map(Map.Entry::getValue)
                .filter(split -> split.getDate().isAfter(exDate))
                .map(Split::getMultiplier)
                .reduce(BigDecimal.ONE, (aFloat, aFloat2) -> aFloat.multiply(aFloat2)).multiply(amount);
    }
}
