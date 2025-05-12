package ru.nataliaoskina.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotUser {

    private UUID id;
    private Long telegramUserId;
    private Long telegramChatId;
    private String username;
    private String firstName;
    private String lastName;
    private UUID currentMenuId;
    @Builder.Default
    private String history = "[]";
}
