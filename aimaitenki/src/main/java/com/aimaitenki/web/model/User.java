package com.aimaitenki.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 200)
    private String password; // BCrypt

    @Column(length = 100)
    private String home;

    @Column(length = 100)
    private String destination;

    @Column(length = 50)
    private String role = "USER";

    // --- getters/setters ---
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        this.username = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        this.password = v;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String v) {
        this.home = v;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String v) {
        this.destination = v;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String v) {
        this.role = v;
    }
}
