package ru.nataliaoskina.listener;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotListener extends TelegramLongPollingBot {

    public TelegramBotsApi telegramBotsApi;
    @Value("${telegram.botUserName}")
    private String botUserName;

    public TelegramBotListener(DefaultBotOptions defaultBotOptions,
                               TelegramBotsApi telegramBotsApi,
                               @Value("${telegram.token}") String token) {
        super(defaultBotOptions, token);
        this.telegramBotsApi = telegramBotsApi;
    }

    @PostConstruct
    void init() throws TelegramApiException {
        telegramBotsApi.registerBot(this);
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());
            execute(message);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
