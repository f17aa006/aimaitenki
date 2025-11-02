package com.aimaitenki.web.controller;

import com.aimaitenki.web.repository.UserRepository;
import com.aimaitenki.web.service.WeatherService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PreferencesController {

    private final UserRepository userRepository;
    private final WeatherService weatherService;

    public PreferencesController(UserRepository userRepository, WeatherService weatherService) {
        this.userRepository = userRepository;
        this.weatherService = weatherService;
    }

    /** 住んでいる場所/目的地の保存 + 天気アドバイス返却（JSON） */
    @PostMapping("/preferences")
    public Map<String, Object> savePreferences(
            @RequestParam(required = false) String home,
            @RequestParam(required = false) String destination,
            Authentication auth) {
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("ログインユーザーが見つかりません"));

        if (home != null)
            user.setHome(home);
        if (destination != null)
            user.setDestination(destination);
        userRepository.save(user);

        boolean homeWillRain = (home != null && !home.isBlank()) && weatherService.getWeather(home);
        boolean destWillRain = (destination != null && !destination.isBlank())
                && weatherService.getWeather(destination);

        String advice = weatherService.buildAdvice(home, destination);

        return Map.of(
                "saved", true,
                "home", user.getHome(),
                "destination", user.getDestination(),
                "homeWillRain", homeWillRain,
                "destinationWillRain", destWillRain,
                "advice", advice);
    }
}
