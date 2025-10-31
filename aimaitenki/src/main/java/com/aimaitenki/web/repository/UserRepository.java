package com.aimaitenki.web.repository;

import com.aimaitenki.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 👇 これが抜けていた！
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
