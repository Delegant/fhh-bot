package ru.nataliaoskina.di;

import dagger.Module;
import dagger.Provides;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.webhook.TelegramBotsWebhookApplication;
import org.telegram.telegrambots.webhook.WebhookOptions;
import ru.nataliaoskina.configuration.properties.AppProperties;
import ru.nataliaoskina.listener.BotListener;
import ru.nataliaoskina.handlers.api.UpdateHandler;

import javax.inject.Singleton;
import java.util.Set;

@Module
@Slf4j
public class BotModule {

    @Provides
    @Singleton
    public BotListener provideBotHandlers(Set<UpdateHandler> updateHandler, AppProperties properties) {
        return new BotListener(updateHandler, properties);
    }

    @Provides
    @Singleton
    public TelegramBotsWebhookApplication provideTelegramBotsWebhookApplication(WebhookOptions webhookOptions, BotListener botListener) {
        try {
            TelegramBotsWebhookApplication webhookApplication = new TelegramBotsWebhookApplication(webhookOptions);
            webhookApplication.registerBot(botListener);
            return webhookApplication;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public WebhookOptions provideWebhookOptions(AppProperties config) {
        return WebhookOptions
                .builder()
                .useHttps(true)
                .keyStorePath(config.telegramBot().keystorePath())
                .keyStorePassword(config.telegramBot().keystorePassword())
                .enableRequestLogging(true)
                .build();
    }
}
