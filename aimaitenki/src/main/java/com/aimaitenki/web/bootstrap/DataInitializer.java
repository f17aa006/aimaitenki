package com.aimaitenki.web.bootstrap;

import com.aimaitenki.web.model.User;
import com.aimaitenki.web.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // 検証ユーザー: username=test / password=test1234
        if (!repo.existsByUsername("test")) {
            User u = new User();
            u.setUsername("test");
            u.setPassword(encoder.encode("test1234")); // BCryptでハッシュ
            u.setRole("ROLE_USER");
            repo.save(u);
            System.out.println("[INIT] demo user created -> test / test1234");
        }
    }
}
