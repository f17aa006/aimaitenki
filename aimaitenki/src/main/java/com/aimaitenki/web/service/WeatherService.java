package com.aimaitenki.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WeatherService {

    private final WebClient webClient;

    @Value("${weatherapi.key}")
    private String apiKey;

    public WeatherService() {
        this.webClient = WebClient.create("https://api.weatherapi.com/v1");
    }

    public String getWeather(String city) {
        try {
            JsonNode json = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/current.json")
                            .queryParam("key", apiKey)
                            .queryParam("q", city)
                            .queryParam("lang", "ja")
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            String condition = json.get("current").get("condition").get("text").asText();
            double temp = json.get("current").get("temp_c").asDouble();

            // ☔ 傘判定（キーワードマッチで柔軟に判定）
            boolean needUmbrella = condition.contains("雨") || condition.contains("雷") || condition.contains("嵐");

            String advice = needUmbrella ? "☔ 雨が降りそうです！傘を持って行きましょう。"
                    : "🌤 傘は不要です。いい一日を！";

            return String.format("""
                    🌏 %s の現在の天気
                    ───────────────────
                    状況: %s
                    気温: %.1f℃
                    %s
                    """, city, condition, temp, advice);

        } catch (Exception e) {
            return "天気情報を取得できませんでした: " + e.getMessage();
        }
    }
}
