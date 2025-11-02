package com.aimaitenki.web.controller;

import com.aimaitenki.web.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /** 文字メッセージが欲しい場合（既存のUI想定に合わせる） */
    @GetMapping("/advice")
    public String advice(@RequestParam(required = false) String home,
            @RequestParam(required = false) String destination) {
        return weatherService.buildAdvice(home, destination);
    }

    /** boolean が欲しい場合のAPI（機械可読） */
    @GetMapping("/will-rain")
    public Map<String, Object> willRain(@RequestParam String q) {
        boolean will = weatherService.getWeather(q); // boolean
        return Map.of("location", q, "willRainToday", will);
    }
}
