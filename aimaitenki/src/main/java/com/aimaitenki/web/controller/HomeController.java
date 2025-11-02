package com.aimaitenki.web.controller;

import com.aimaitenki.web.repository.UserRepository;
import com.aimaitenki.web.service.WeatherService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final WeatherService weatherService;

    public HomeController(UserRepository userRepository, WeatherService weatherService) {
        this.userRepository = userRepository;
        this.weatherService = weatherService;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication auth) {
        var user = userRepository.findByUsername(auth.getName()).orElseThrow();
        String home = user.getHome();
        String dest = user.getDestination();

        String advice = weatherService.buildAdvice(home, dest);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("home", home);
        model.addAttribute("destination", dest);
        model.addAttribute("advice", advice); // ← これを画面に出す
        return "home";
    }
}
