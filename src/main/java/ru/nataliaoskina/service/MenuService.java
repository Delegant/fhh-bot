package ru.nataliaoskina.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import ru.nataliaoskina.domain.model.BotUser;
import ru.nataliaoskina.domain.model.MenuNode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuService {

    private final MenuNodeService menuNodeService;
    private final BotUserService botUserService;
    private final ObjectMapper objectMapper;

    @Inject
    public MenuService(MenuNodeService menuNodeService, BotUserService botUserService, ObjectMapper objectMapper) {
        this.menuNodeService = menuNodeService;
        this.botUserService = botUserService;
        this.objectMapper = objectMapper;
    }

    public InlineKeyboardMarkup buildMenu(BotUser orCreateBotUser) {
        UUID currentNodeId = orCreateBotUser.getCurrentMenuId();
        MenuNode currentMenuNode = menuNodeService.findById(currentNodeId);
        var groupedByRowMenu   = menuNodeService.findChildren(currentNodeId).stream()
                .sorted(Comparator.comparingInt(MenuNode::getButtonRow)
                        .thenComparingInt(MenuNode::getButtonColumn))
                .collect(Collectors.groupingBy(MenuNode::getButtonRow, LinkedHashMap::new, Collectors.toList()));

        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (List<MenuNode> row : groupedByRowMenu.values()) {

            List<InlineKeyboardButton> rowButtons = row.stream()
                    .map(node -> button(node.getLabel(), node.getKey()))
                    .collect(Collectors.toList());

            rows.add(new InlineKeyboardRow(rowButtons));
        }

        InlineKeyboardRow upBackRow = new InlineKeyboardRow();
        if (hasHistory(orCreateBotUser)) {
            upBackRow.add(button("⬅ Назад", "back"));
        }
        if (currentMenuNode.getParentId() != null) {
            upBackRow.add(button("⬆ Вверх", "up"));
        }

        rows.add(upBackRow);
        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }

    public void moveTo(BotUser botUser, String key) {
        MenuNode nextNode = menuNodeService.findMenuNodeById(key);
        pushToHistory(botUser);
        botUser.setCurrentMenuId(nextNode.getId());
        botUserService.save(botUser);
    }

    public void back(BotUser botUser) {
        List<UUID> history = getHistoryList(botUser);
        if (!history.isEmpty()) {
            UUID lastUuid = history.remove(history.size() - 1);
            MenuNode previousMenuNode = menuNodeService.findById(lastUuid);
            botUser.setCurrentMenuId(previousMenuNode.getId());
            botUser.setHistory(toJson(history));
            botUserService.save(botUser);
        }
    }

    public void up(BotUser botUser) {
        MenuNode currentMenuNode = menuNodeService.findById(botUser.getCurrentMenuId());
        if (currentMenuNode.getParentId() != null) {
            MenuNode parentCurrentMenuNode = menuNodeService.findById(currentMenuNode.getParentId());
            pushToHistory(botUser);
            botUser.setCurrentMenuId(parentCurrentMenuNode.getId());
            botUserService.save(botUser);
        }
    }

    private void pushToHistory(BotUser botUser) {
        List<UUID> history = getHistoryList(botUser);
        history.add(botUser.getCurrentMenuId());
        botUser.setHistory(toJson(history));
    }

    private List<UUID> getHistoryList(BotUser position) {
        try {
            return objectMapper.readValue(position.getHistory(), new TypeReference<List<UUID>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    private boolean hasHistory(BotUser botUser) {
        return !getHistoryList(botUser).isEmpty();
    }

    private InlineKeyboardButton button(String label, String callbackData) {
        return InlineKeyboardButton
                .builder()
                .text(label)
                .callbackData("menu:" + callbackData)
                .build();
    }
}
