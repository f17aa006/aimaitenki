package com.aimaitenki.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

/**
 * Stores the user's frequently used locations.
 * The values are stored in a simple properties file.
 */
public final class UserPreferences {
    private static final String HOME_KEY = "home";
    private static final String DESTINATION_KEY = "destination";

    private final Path storagePath;
    private String homeLocation;
    private String destination;

    private UserPreferences(Path storagePath, String homeLocation, String destination) {
        this.storagePath = storagePath;
        this.homeLocation = homeLocation;
        this.destination = destination;
    }

    public static UserPreferences load(Path path) throws IOException {
        Properties properties = new Properties();
        if (Files.exists(path)) {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        } else {
            Files.createDirectories(path.getParent());
        }
        String home = properties.getProperty(HOME_KEY, "");
        String destination = properties.getProperty(DESTINATION_KEY, "");
        return new UserPreferences(path, home, destination);
    }

    public void save() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(HOME_KEY, Objects.toString(homeLocation, ""));
        properties.setProperty(DESTINATION_KEY, Objects.toString(destination, ""));
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(storagePath), StandardCharsets.UTF_8)) {
            properties.store(writer, "User preferences for the weather advisor application");
        }
    }

    public String getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(String homeLocation) {
        this.homeLocation = Objects.requireNonNullElse(homeLocation, "").trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = Objects.requireNonNullElse(destination, "").trim();
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
                "homeLocation='" + homeLocation + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
