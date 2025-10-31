package com.aimaitenki.web;

import com.aimaitenki.app.*;
import static spark.Spark.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebServer {
    public static void main(String[] args) {
        port(8080);
        get("/", (req, res) -> "ようこそ！ /advice?home=東京&destination=福岡 にアクセスしてください。");

        get("/advice", (req, res) -> {
            String home = req.queryParams("home");
            String dest = req.queryParams("destination");

            Path forecastPath = Paths.get("data", "forecasts.csv");
            ForecastRepository repo = new ForecastRepository(forecastPath);
            WeatherAdvisor advisor = new WeatherAdvisor(repo);

            UserPreferences pref = UserPreferences.load(
                    Paths.get(System.getProperty("user.home"), ".aimaitenki", "preferences.properties"));
            if (home != null)
                pref.setHomeLocation(home);
            if (dest != null)
                pref.setDestination(dest);

            WeatherAdvisor.AdviceResult result = advisor.evaluate(pref);
            res.type("text/html; charset=UTF-8");
            return "<h1>" + result.message() + "</h1>";
        });
    }
}
