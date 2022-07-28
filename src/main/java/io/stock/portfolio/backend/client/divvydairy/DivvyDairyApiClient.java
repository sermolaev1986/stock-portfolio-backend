package io.stock.portfolio.backend.client.divvydairy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DivvyDairyApiClient {

    @Value("${divvydairy-api-key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    public List<DivvyDairyDividend> getUpcomingDividends() {
        String url = "https://api.divvydiary.com/dividends/upcoming";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("currency", "EUR")
                .queryParam("userId", "67556");

        final LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set("X-API-KEY", apiKey);
        headers.set("Accept", "application/json");
        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                DivvyDairyResponse.class).getBody().getDividends();

    }

}
