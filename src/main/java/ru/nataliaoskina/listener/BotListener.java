package ru.nataliaoskina.listener;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.TelegramWebhookBot;
import ru.nataliaoskina.configuration.properties.AppProperties;
import ru.nataliaoskina.handlers.api.UpdateHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class BotListener implements TelegramWebhookBot {
    private final AppProperties.TelegramBot botProperties;
    private final TelegramClient telegramClient;
    private final List<UpdateHandler> updateHandlers;

    @Inject
    public BotListener(Set<UpdateHandler> updateHandlers, AppProperties properties) {
        this.telegramClient = new OkHttpTelegramClient(properties.telegramBot().token());
        this.updateHandlers = new ArrayList<>(updateHandlers);
        this.botProperties = properties.telegramBot();
    }

    @Override
    public BotApiMethod<?> consumeUpdate(Update update) {
        return updateHandlers.stream()
                .filter(h -> h.canHandle(update))
                .findFirst()
                .map(h -> h.handle(update))
                .orElse(null);
    }

    @Override
    public void runDeleteWebhook() {
        try {
            telegramClient.execute(new DeleteWebhook());
        } catch (TelegramApiException e) {
            log.info("Error deleting webhook");
        }
    }

    @Override
    public void runSetWebhook() {
        try {
            telegramClient.execute(SetWebhook
                    .builder()
                    .url(botProperties.webhookUrl() + getBotPath())
                    .build());
        } catch (TelegramApiException e) {
            log.info("Error setting webhook");
        }
    }

    @Override
    public String getBotPath() {
        return "/" + botProperties.path();
    }
}
