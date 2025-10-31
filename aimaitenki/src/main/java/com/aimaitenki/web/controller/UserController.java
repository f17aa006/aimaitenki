package com.aimaitenki.web.controller;

import com.aimaitenki.web.model.User;
import com.aimaitenki.web.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // 🔹 すでに同名ユーザーが存在するかチェック
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return "⚠️ ユーザー名 '" + user.getUsername() + "' はすでに登録されています。";
        }

        // 🔹 パスワードを暗号化して保存
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "✅ 登録完了: " + user.getUsername();
    }
}
