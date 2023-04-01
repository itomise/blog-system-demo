# ビルドステージ
FROM openjdk:17-slim as build
WORKDIR /app

COPY . .

RUN ./gradlew :adapters:adminApi:build -x test

# ランタイムステージ
FROM openjdk:17-slim

WORKDIR /app

# 非特権ユーザーを作成
RUN groupadd -r appuser && useradd -r -g appuser appuser

# JAR ファイルをコピー
COPY --from=builder /app/adapters/adminApi/build/libs/*-all.jar /app/app.jar
COPY ./src/main/resources/application.prod.conf /app/application.prod.conf

# ファイルの所有権を appuser に変更
RUN chown -R appuser:appuser /app

# ユーザーを appuser に切り替え
USER appuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "-config=application.prod.conf"]
