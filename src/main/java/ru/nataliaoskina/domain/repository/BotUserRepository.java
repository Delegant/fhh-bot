package ru.nataliaoskina.domain.repository;

import org.jdbi.v3.core.Jdbi;
import ru.nataliaoskina.domain.dao.BotUserDao;
import ru.nataliaoskina.domain.model.BotUser;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public class BotUserRepository {

    private final BotUserDao botUserDao;

    @Inject
    public BotUserRepository(Jdbi jdbi) {
        this.botUserDao = jdbi.onDemand(BotUserDao.class);
    }

    public BotUser save(BotUser user) {
        botUserDao.upsert(user);
        return user;
    }

    public Optional<BotUser> findById(UUID uuid) {
        return botUserDao.findById(uuid);
    }

    public Optional<BotUser> findByTelegramId(Long telegramUserId) {
        return botUserDao.findByTelegramId(telegramUserId);
    }
}
