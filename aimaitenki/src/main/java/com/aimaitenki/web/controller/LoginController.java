package com.aimaitenki.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // GET /login → templates/login.html を返す
    @GetMapping("/login-page")
    public String login() {
        return "login";
    }

}
