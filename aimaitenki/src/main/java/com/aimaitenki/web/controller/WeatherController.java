package com.aimaitenki.web.controller;

import com.aimaitenki.web.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/{city}", produces = "text/html; charset=UTF-8")
    public String getWeather(@PathVariable String city) {
        String result = weatherService.getWeather(city);
        return "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>天気アドバイザー</title>" +
                "<style>" +
                "body { font-family:'Segoe UI','Hiragino Sans',sans-serif; " +
                "background:linear-gradient(to bottom right,#a3d8f4,#e6f2ff);" +
                "text-align:center; padding:3em; }" +
                ".card { background:white; border-radius:12px; " +
                "box-shadow:0 4px 10px rgba(0,0,0,0.2); padding:2em;" +
                "display:inline-block; max-width:400px; }" +
                ".emoji { font-size:3em; }" +
                "</style>" +
                "</head><body>" +
                "<div class='card'><div class='emoji'>🌦️</div>" +
                "<pre style='text-align:left;white-space:pre-wrap;'>" + result + "</pre>" +
                "</div></body></html>";
    }
}
