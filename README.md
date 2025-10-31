# シンプルな天気アドバイザー

住んでいる場所と目的地の天気を調べて、傘が必要かどうかを教えてくれるシンプルな Java アプリです。

## 事前準備

* Java 17 以上がインストールされていることを想定しています。

## 使い方

1. 初回起動時に `~/.aimaitenki/preferences.properties` と `data/forecasts.csv` が利用されます。
   * `preferences.properties` には住んでいる場所 (`home`) と目的地 (`destination`) を保存します。レポジトリ直下の `config/preferences.properties` はサンプルです。
   * `forecasts.csv` には "場所,天気" 形式で予報を記述します。天気は `SUNNY`, `CLOUDY`, `RAIN`, `SNOW`, `STORM` のいずれかです。
2. 住んでいる場所や目的地を更新する場合は以下のように実行します。

   ```bash
   javac -d out $(find src/main/java -name "*.java")
   java -cp out com.aimaitenki.app.Main --set-home 東京 --set-destination 福岡
   ```

   設定は `~/.aimaitenki/preferences.properties` に保存され、次回以降の実行で再利用されます。

3. 傘が必要かどうかを確認するには引数なしで実行します。

   ```bash
   java -cp out com.aimaitenki.app.Main
   ```

## サンプル出力

```
設定を保存しました: UserPreferences{homeLocation='東京', destination='福岡'}
傘が必要です。 (雨が予想される: 住んでいる場所、目的地)
```

予報ファイルに情報がない場合は注意メッセージが表示されます。

```
傘は不要です。 天気情報が見つかりません: 目的地。
```

## ファイル構成

* `src/main/java/com/aimaitenki/app` - アプリ本体のソースコード
* `config/preferences.properties` - 設定ファイルのサンプル
* `data/forecasts.csv` - シンプルな予報データベース

必要に応じてこれらのファイルを編集することでアプリをカスタマイズできます。
