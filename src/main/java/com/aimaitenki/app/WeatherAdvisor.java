 (cd "$(git rev-parse --show-toplevel)" && git apply --3way <<'EOF' 
diff --git a/src/main/java/com/aimaitenki/app/WeatherAdvisor.java b/src/main/java/com/aimaitenki/app/WeatherAdvisor.java
new file mode 100644
index 0000000000000000000000000000000000000000..53dd43bb30a447eeb4f5c141ead9932148d4a6af
--- /dev/null
+++ b/src/main/java/com/aimaitenki/app/WeatherAdvisor.java
@@ -0,0 +1,66 @@
+package com.aimaitenki.app;
+
+import java.io.IOException;
+import java.util.ArrayList;
+import java.util.LinkedHashMap;
+import java.util.List;
+import java.util.Map;
+import java.util.Optional;
+
+/**
+ * Evaluates the weather for the saved locations and returns human readable advice.
+ */
+public class WeatherAdvisor {
+    private final ForecastRepository forecastRepository;
+
+    public WeatherAdvisor(ForecastRepository forecastRepository) {
+        this.forecastRepository = forecastRepository;
+    }
+
+    public AdviceResult evaluate(UserPreferences preferences) throws IOException {
+        Map<String, Optional<WeatherCondition>> conditions = new LinkedHashMap<>();
+        conditions.put("住んでいる場所", forecastRepository.findCondition(preferences.getHomeLocation()));
+        conditions.put("目的地", forecastRepository.findCondition(preferences.getDestination()));
+
+        List<String> rainyLocations = new ArrayList<>();
+        List<String> unknownLocations = new ArrayList<>();
+
+        for (Map.Entry<String, Optional<WeatherCondition>> entry : conditions.entrySet()) {
+            Optional<WeatherCondition> value = entry.getValue();
+            if (value.isEmpty()) {
+                unknownLocations.add(entry.getKey());
+            } else if (value.get().requiresUmbrella()) {
+                rainyLocations.add(entry.getKey());
+            }
+        }
+
+        boolean needsUmbrella = !rainyLocations.isEmpty();
+
+        StringBuilder message = new StringBuilder();
+        if (needsUmbrella) {
+            message.append("傘が必要です。");
+        } else {
+            message.append("傘は不要です。");
+        }
+
+        if (!rainyLocations.isEmpty()) {
+            message.append(" (雨が予想される: ");
+            message.append(String.join("、", rainyLocations));
+            message.append(")");
+        }
+
+        if (!unknownLocations.isEmpty()) {
+            message.append(" 天気情報が見つかりません: ");
+            message.append(String.join("、", unknownLocations));
+            message.append("。");
+        }
+
+        return new AdviceResult(needsUmbrella, rainyLocations, unknownLocations, message.toString());
+    }
+
+    public record AdviceResult(boolean needsUmbrella,
+                               List<String> rainyLocations,
+                               List<String> unknownLocations,
+                               String message) {
+    }
+}
 
EOF
)
