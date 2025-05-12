package ru.nataliaoskina.configuration.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppProperties(
        Datasource datasource,
        Liquibase liquibase,
        TelegramBot telegramBot,
        Logging logging
) {

    public record Datasource(
            String driver,
            String username,
            String password,
            String url,
            Pool pool
    ) {

        public record Pool(
                Integer maximumPoolSize,
                Integer minimumIdle,
                Integer idleTimeoutMs,
                Integer maxLifetimeMs
        ) {}
    }

    public record Liquibase(
            String changeLog,
            boolean enabled
    ) {}

    public record TelegramBot(
            String name,
            String token,
            String webhookUrl,
            String path,
            String keystorePath,
            String keystorePassword
    ) {}

    public record Logging(
            Map<String, String> level
    ) {}
}
