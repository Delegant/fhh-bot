package ru.nataliaoskina.handlers.api;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    BotApiMethod<?> handle(Update update);

    Boolean canHandle(Update update);
}
