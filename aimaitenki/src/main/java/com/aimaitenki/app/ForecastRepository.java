package com.aimaitenki.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Loads simple weather forecasts from a CSV file.
 * The format is "location,condition" with UTF-8 encoding.
 * Lines starting with a hash (#) are treated as comments and ignored.
 */
public class ForecastRepository {
    private final Path csvPath;
    private Map<String, WeatherCondition> cache;

    public ForecastRepository(Path csvPath) {
        this.csvPath = csvPath;
    }

    public synchronized Map<String, WeatherCondition> loadAll() throws IOException {
        if (cache != null) {
            return cache;
        }
        ensureSampleFileExists();

        Map<String, WeatherCondition> result = new LinkedHashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

                String[] parts = trimmed.split(",");
                if (parts.length != 2) continue;

                String location = parts[0].trim();
                String conditionText = parts[1].trim();

                if (!location.isEmpty() && !conditionText.isEmpty()) {
                    try {
                        WeatherCondition condition = WeatherCondition.fromText(conditionText);
                        result.put(location, condition);
                    } catch (IllegalArgumentException ignored) {
                        // skip invalid entries
                    }
                }
            }
        }
        cache = Collections.unmodifiableMap(result);
        return cache;
    }

    public Optional<WeatherCondition> findCondition(String location) throws IOException {
        if (location == null || location.trim().isEmpty()) {
            return Optional.empty();
        }
        Map<String, WeatherCondition> data = loadAll();
        WeatherCondition condition = data.get(location);
        if (condition != null) {
            return Optional.of(condition);
        }
        condition = data.get(location.trim());
        if (condition != null) {
            return Optional.of(condition);
        }
        String normalized = location.trim().toLowerCase(Locale.ROOT);
        for (Map.Entry<String, WeatherCondition> entry : data.entrySet()) {
            if (entry.getKey().trim().toLowerCase(Locale.ROOT).equals(normalized)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    private void ensureSampleFileExists() throws IOException {
        if (Files.exists(csvPath)) return;

        Files.createDirectories(csvPath.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)) {
            writer.write("# location,condition\n");
            writer.write("東京,RAIN\n");
            writer.write("大阪,CLOUDY\n");
            writer.write("名古屋,SUNNY\n");
        }
    }
}
