package com.aimaitenki.web.controller;

import com.aimaitenki.web.model.User;
import com.aimaitenki.web.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ★ 画面返すだけ。GET /login, GET /signup を必ず用意
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; // templates/signup.html
    }

    // ★ サインアップ実行（POST）
    @PostMapping("/signup")
    public String doSignup(@RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String home,
            @RequestParam(required = false) String destination) {

        if (userRepository.existsByUsername(username)) {
            return "redirect:/signup?error=exists";
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setHome(home);
        u.setDestination(destination);
        u.setRole("USER");
        userRepository.save(u);

        return "redirect:/login?signup";
    }
}
