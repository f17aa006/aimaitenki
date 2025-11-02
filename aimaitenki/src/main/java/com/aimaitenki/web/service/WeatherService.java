package com.aimaitenki.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final RestClient client;

    @Value("${weatherapi.key:}")
    private String apiKey;

    public WeatherService(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://api.weatherapi.com/v1").build();
    }

    public boolean getWeather(String location) {
        return willRainToday(location);
    }

    public boolean willRainToday(String location) {
        if (location == null || location.isBlank()) {
            log.debug("willRainToday: location is blank");
            return false;
        }
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("weatherapi.key が未設定。常に false を返します。");
            return false;
        }
        try {
            JsonNode root = client.get()
                    .uri(uri -> uri.path("/forecast.json")
                            .queryParam("key", apiKey)
                            .queryParam("q", location)
                            .queryParam("days", "1")
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

            if (root == null) {
                log.warn("WeatherAPI 応答が null");
                return false;
            }

            JsonNode day = root.at("/forecast/forecastday/0/day");
            int will = day.path("daily_will_it_rain").asInt(0);
            int chance = day.path("daily_chance_of_rain").asInt(0);
            log.debug("WeatherAPI: location={} will={} chance={}", location, will, chance);
            return will == 1 || chance >= 50;

        } catch (RestClientResponseException e) {
            log.warn("WeatherAPI エラー status={} body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.warn("WeatherAPI 呼び出し失敗", e);
            return false;
        }
    }

    public boolean needUmbrella(String home, String destination) {
        boolean homeRain = (home != null && !home.isBlank()) && willRainToday(home);
        boolean destRain = (destination != null && !destination.isBlank()) && willRainToday(destination);
        return homeRain || destRain;
    }

    public String buildAdvice(String home, String destination) {
        boolean need = needUmbrella(home, destination);
        if (need) {
            String where = "";
            boolean noHome = (home == null || home.isBlank());
            boolean noDest = (destination == null || destination.isBlank());
            where = (noHome ? "" : "住んでいる場所")
                    + (!noHome && !noDest ? "、" : "")
                    + (noDest ? "" : "目的地");
            if (where.isBlank())
                where = "いずれかの地点";
            return "傘が必要です。（雨が予想される: " + where + "）";
        } else {
            return "傘は不要です。";
        }
    }
}
