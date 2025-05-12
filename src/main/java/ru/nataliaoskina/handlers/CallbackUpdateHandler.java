package ru.nataliaoskina.handlers;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.nataliaoskina.domain.model.BotUser;
import ru.nataliaoskina.handlers.api.UpdateHandler;
import ru.nataliaoskina.service.BotUserService;
import ru.nataliaoskina.service.MenuService;

import javax.inject.Inject;

@Slf4j
public class CallbackUpdateHandler implements UpdateHandler {
    public static final String MENU_PREFIX = "menu:";
    private final MenuService menuService;
    private final BotUserService botUserService;

    @Inject
    public CallbackUpdateHandler(MenuService menuService, BotUserService botUserService) {
        this.menuService = menuService;
        this.botUserService = botUserService;
    }

    @Override
    public Boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith(MENU_PREFIX);
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        Long telegramUserId = callback.getFrom().getId();
        Long telegramChatId = callback.getMessage().getChatId();
        String data = callback.getData();
        BotUser botUser = botUserService.getOrCreateBotUser(telegramUserId, telegramChatId);

        log.info("Получен апдейт с данными Update.Data = {}", data);
        if (data.startsWith(MENU_PREFIX)) {
            String command = data.substring(MENU_PREFIX.length());
            switch (command) {
                case "back" -> menuService.back(botUser);
                case "up" -> menuService.up(botUser);
                default -> menuService.moveTo(botUser, command);
            }

            InlineKeyboardMarkup markup = menuService.buildMenu(botUser);
            return EditMessageReplyMarkup.builder()
                    .chatId(callback.getMessage().getChatId().toString())
                    .messageId(callback.getMessage().getMessageId())
                    .replyMarkup(markup)
                    .build();
        }

        return null;
    }
}
