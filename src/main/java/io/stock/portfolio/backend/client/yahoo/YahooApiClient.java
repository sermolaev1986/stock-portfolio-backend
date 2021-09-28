package io.stock.portfolio.backend.client.yahoo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class YahooApiClient {

    private final RestTemplate restTemplate;

    public List<YahooDividend> detDividends(String symbol, LocalDateTime lastDividendDate) {
        long from = lastDividendDate.atZone(ZoneId.systemDefault()).toEpochSecond();
        long to = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        String yahooUrl = String.format("https://query1.finance.yahoo.com/v8/finance/chart/%s.F", symbol);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(yahooUrl)
                .queryParam("symbol", symbol)
                .queryParam("period1", from)
                .queryParam("period2", to)
                .queryParam("interval", "1mo")
                .queryParam("includePrePost", true)
                .queryParam("events", "div,split");


        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(new LinkedMultiValueMap<>()),
                Response.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return convertToYahooDividend(Objects.requireNonNull(responseEntity.getBody()));
        }

        return Collections.emptyList();
    }

    private List<YahooDividend> convertToYahooDividend(Response response) {

        return response.getChart().getResult()
                .stream()
                .map(Result::getEvents)
                .filter(Objects::nonNull)
                .map(Events::getDividends)
                .filter(Objects::nonNull)
                .flatMap(div -> div.entrySet().stream())
                .map(div -> {
                    Dividend value = div.getValue();

                    YahooDividend yahooDividend = new YahooDividend();
                    yahooDividend.setAmount(value.getAmount());

                    Instant instant = Instant.ofEpochSecond(value.getDate());
                    LocalDateTime exDate = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                    yahooDividend.setExDate(exDate);

                    return yahooDividend;
                })
                .collect(Collectors.toList());
    }
}
