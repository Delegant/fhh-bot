package ru.nataliaoskina.handlers;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.nataliaoskina.domain.model.BotUser;
import ru.nataliaoskina.handlers.api.UpdateHandler;
import ru.nataliaoskina.service.BotUserService;
import ru.nataliaoskina.service.MenuService;

import javax.inject.Inject;

@Slf4j
public class StartCommandUpdateHandler implements UpdateHandler {

    private final MenuService menuService;
    private final BotUserService botUserService;

    @Inject
    public StartCommandUpdateHandler(MenuService menuService, BotUserService botUserService) {
        this.menuService = menuService;
        this.botUserService = botUserService;
    }

    @Override
    public Boolean canHandle(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equals("/start");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long telegramUserId = update.getMessage().getFrom().getId();
        Long telegramChatId = update.getMessage().getChatId();
        BotUser user = botUserService.getOrCreateBotUser(telegramUserId, telegramChatId);

        InlineKeyboardMarkup markup = menuService.buildMenu(user);

        return SendMessage
                .builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("Well, all information looks like noise until you break the code.")
                .replyMarkup(markup)
                .build();
    }
}
