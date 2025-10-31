 (cd "$(git rev-parse --show-toplevel)" && git apply --3way <<'EOF' 
diff --git a/src/main/java/com/aimaitenki/app/WeatherCondition.java b/src/main/java/com/aimaitenki/app/WeatherCondition.java
new file mode 100644
index 0000000000000000000000000000000000000000..258f10f1a246844e184c20a66f586989da085cd1
--- /dev/null
+++ b/src/main/java/com/aimaitenki/app/WeatherCondition.java
@@ -0,0 +1,45 @@
+package com.aimaitenki.app;
+
+/**
+ * Represents simplified weather conditions that the application understands.
+ */
+public enum WeatherCondition {
+    SUNNY(false),
+    CLOUDY(false),
+    RAIN(true),
+    SNOW(true),
+    STORM(true);
+
+    private final boolean requiresUmbrella;
+
+    WeatherCondition(boolean requiresUmbrella) {
+        this.requiresUmbrella = requiresUmbrella;
+    }
+
+    /**
+     * Returns whether the condition implies that the user should carry an umbrella.
+     */
+    public boolean requiresUmbrella() {
+        return requiresUmbrella;
+    }
+
+    /**
+     * Parses a condition name from text. The method is case-insensitive and trims whitespace.
+     *
+     * @param value The textual representation of the condition.
+     * @return The parsed {@link WeatherCondition}.
+     * @throws IllegalArgumentException if the value does not match a known condition.
+     */
+    public static WeatherCondition fromText(String value) {
+        if (value == null) {
+            throw new IllegalArgumentException("Condition cannot be null");
+        }
+        String normalized = value.trim().toUpperCase();
+        for (WeatherCondition condition : values()) {
+            if (condition.name().equals(normalized)) {
+                return condition;
+            }
+        }
+        throw new IllegalArgumentException("Unknown weather condition: " + value);
+    }
+}
 
EOF
)
