package com.aimaitenki.web;

import com.aimaitenki.app.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class AdviceController {

    @GetMapping("/advice")
    public String getAdvice(
            @RequestParam(defaultValue = "東京") String home,
            @RequestParam(defaultValue = "福岡") String destination) throws IOException {

        Path forecastPath = Paths.get("data", "forecasts.csv");
        ForecastRepository repo = new ForecastRepository(forecastPath);
        WeatherAdvisor advisor = new WeatherAdvisor(repo);

        UserPreferences pref = UserPreferences.load(
                Paths.get(System.getProperty("user.home"), ".aimaitenki", "preferences.properties"));
        pref.setHomeLocation(home);
        pref.setDestination(destination);

        WeatherAdvisor.AdviceResult result = advisor.evaluate(pref);
        return result.message();
    }
}
