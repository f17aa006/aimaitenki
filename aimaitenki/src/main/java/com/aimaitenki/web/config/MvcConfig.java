package com.aimaitenki.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login"); // GET /login -> templates/login.html
        registry.addViewController("/signup").setViewName("signup"); // GET /signup -> templates/signup.html
        registry.addViewController("/home").setViewName("home"); // 任意
    }
}
