package io.stock.portfolio.backend.client.exchangerate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    private static final String BASE_PATH = "http://api.exchangeratesapi.io/v1/{date}";

    @Value("${api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Float getByDate(LocalDateTime exDate) {
        String formattedDate = dateTimeFormatter.format(exDate);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_PATH)
                .queryParam("access_key", apiKey);

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("date", formattedDate);
        builder.build(urlParams);


        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                builder.buildAndExpand(urlParams).toUri(),
                HttpMethod.GET,
                new HttpEntity<>(new LinkedMultiValueMap<>()),
                Response.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return convertToRate(Objects.requireNonNull(responseEntity.getBody()));
        }
        return 0.0f;
    }

    private Float convertToRate(Response response) {
        return response.getRates().getUSD();
    }
}
