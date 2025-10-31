package com.aimaitenki.app;

/**
 * Represents simplified weather conditions that the application understands.
 */
public enum WeatherCondition {
    SUNNY(false),
    CLOUDY(false),
    RAIN(true),
    SNOW(true),
    STORM(true);

    private final boolean requiresUmbrella;

    WeatherCondition(boolean requiresUmbrella) {
        this.requiresUmbrella = requiresUmbrella;
    }

    public boolean requiresUmbrella() {
        return requiresUmbrella;
    }

    public static WeatherCondition fromText(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        String normalized = value.trim().toUpperCase();
        for (WeatherCondition condition : values()) {
            if (condition.name().equals(normalized)) {
                return condition;
            }
        }
        throw new IllegalArgumentException("Unknown weather condition: " + value);
    }
}
