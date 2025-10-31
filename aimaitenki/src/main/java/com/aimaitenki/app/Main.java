package com.aimaitenki.app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entry point for the simple weather advisor application.
 */
public final class Main {
    private static final Path PREFERENCES_PATH =
            Paths.get(System.getProperty("user.home"), ".aimaitenki", "preferences.properties");
    private static final Path FORECASTS_PATH = Paths.get("data", "forecasts.csv");

    public static void main(String[] args) {
        Main app = new Main();
        try {
            app.run(args);
        } catch (IOException e) {
            System.err.println("設定ファイルの読み書きでエラーが発生しました: " + e.getMessage());
            System.exit(1);
        }
    }

    private void run(String[] args) throws IOException {
        UserPreferences preferences = UserPreferences.load(PREFERENCES_PATH);
        boolean updated = applyArguments(args, preferences);
        if (updated) {
            preferences.save();
            System.out.println("設定を保存しました: " + preferences);
        }

        ForecastRepository repository = new ForecastRepository(FORECASTS_PATH);
        WeatherAdvisor advisor = new WeatherAdvisor(repository);
        WeatherAdvisor.AdviceResult result = advisor.evaluate(preferences);
        System.out.println(result.message());
    }

    private boolean applyArguments(String[] args, UserPreferences preferences) {
        boolean updated = false;
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            if ("--set-home".equals(argument)) {
                String value = requireValue(args, ++i, "--set-home");
                preferences.setHomeLocation(value);
                updated = true;
            } else if (argument.startsWith("--set-home=")) {
                String value = argument.substring("--set-home=".length());
                preferences.setHomeLocation(value);
                updated = true;
            } else if ("--set-destination".equals(argument)) {
                String value = requireValue(args, ++i, "--set-destination");
                preferences.setDestination(value);
                updated = true;
            } else if (argument.startsWith("--set-destination=")) {
                String value = argument.substring("--set-destination=".length());
                preferences.setDestination(value);
                updated = true;
            } else if ("--help".equals(argument)) {
                printUsage();
                System.exit(0);
            } else {
                System.err.println("未知の引数です: " + argument);
                printUsage();
                System.exit(1);
            }
        }
        return updated;
    }

    private static String requireValue(String[] args, int index, String optionName) {
        if (index >= args.length) {
            System.err.println(optionName + " オプションには値が必要です。");
            printUsage();
            System.exit(1);
        }
        return args[index];
    }

    private static void printUsage() {
        System.out.println("使い方: java com.aimaitenki.app.Main [オプション]");
        System.out.println("オプション:");
        System.out.println("  --set-home <地名>        住んでいる場所を設定します");
        System.out.println("  --set-destination <地名>  目的地を設定します");
        System.out.println("  --help                    このメッセージを表示します");
        System.out.println();
        System.out.println("例: java com.aimaitenki.app.Main --set-home 東京 --set-destination 大阪");
        System.out.println("設定済みの場合は、引数なしで実行すると傘が必要かどうかを表示します。");
    }
}
