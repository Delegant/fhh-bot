package ru.nataliaoskina.di;

import dagger.Component;
import liquibase.Liquibase;
import org.telegram.telegrambots.webhook.TelegramBotsWebhookApplication;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BotModule.class, AppModule.class, BindingsModule.class, DatabaseModule.class})
public interface AppComponent {
    TelegramBotsWebhookApplication getTelegramBotsWebhookApplication();

    Liquibase liquibase();
}
