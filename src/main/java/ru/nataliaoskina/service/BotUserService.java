package ru.nataliaoskina.service;

import lombok.extern.slf4j.Slf4j;
import ru.nataliaoskina.domain.model.BotUser;
import ru.nataliaoskina.domain.repository.BotUserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class BotUserService {
    private final BotUserRepository botUserRepository;
    private final MenuNodeService menuNodeService;

    @Inject
    public BotUserService(BotUserRepository botUserRepository, MenuNodeService menuNodeService) {
        this.botUserRepository = botUserRepository;
        this.menuNodeService = menuNodeService;
    }

    public BotUser getOrCreateBotUser(Long telegramUserId, Long telegramChatId) {
        log.info("Создание нового пользователя с Telegram ID: {}", telegramUserId);
        return botUserRepository.findByTelegramId(telegramUserId).orElseGet(() -> {
            BotUser pos = BotUser.builder()
                    .id(UUID.randomUUID())
                    .telegramUserId(telegramUserId)
                    .currentMenuId(menuNodeService.getRootNode().getId())
                    .telegramChatId(telegramChatId)
                    .build();

            return botUserRepository.save(pos);
        });
    }

    public void registerNewUser(Long chatId) {
        BotUser user = BotUser.builder().telegramChatId(chatId).build();
        user.setTelegramChatId(chatId);
        user.setCurrentMenuId(null);
        botUserRepository.save(user);
    }

    public Optional<BotUser> getById(UUID id) {
        return botUserRepository.findById(id);
    }

    public void save(BotUser botUser) {
        botUserRepository.save(botUser);
    }
}
