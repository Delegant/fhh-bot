package ru.nataliaoskina.domain.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.*;
import ru.nataliaoskina.domain.model.BotUser;

import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(BotUser.class)
public interface BotUserDao {

    @SqlUpdate("""
        INSERT INTO bot_user (id, telegram_user_id, username, current_menu_id, telegram_chat_id)
        VALUES (:id, :telegramUserId, :username, :currentMenuId, :telegramChatId)
        ON CONFLICT (telegram_user_id) DO UPDATE
        SET username = :username, current_menu_id = :currentMenuId, telegram_chat_id = :telegramChatId
    """)
    void upsert(@BindBean BotUser user);

    @SqlQuery("SELECT * FROM bot_user WHERE telegram_user_id = :telegramUserId")
    @RegisterBeanMapper(BotUser.class)
    Optional<BotUser> findByTelegramId(@Bind("telegramUserId") Long telegramUserId);

    @SqlQuery("SELECT * FROM bot_user WHERE id = :id LIMIT 1")
    @RegisterBeanMapper(BotUser.class)
    Optional<BotUser> findById(@Bind("id") UUID id);

    @SqlQuery("SELECT * FROM bot_user WHERE username = :username LIMIT 1")
    @RegisterBeanMapper(BotUser.class)
    Optional<BotUser> findByUsername(@Bind("username") String username);

    @SqlUpdate("UPDATE bot_user SET chat_id = :chatId WHERE id = :id")
    void updateChatId(@Bind("id") UUID id, @Bind("chatId") long chatId);
}
