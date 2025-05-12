package ru.nataliaoskina.service;

import ru.nataliaoskina.domain.model.MenuNode;
import ru.nataliaoskina.domain.repository.MenuNodeRepository;
import ru.nataliaoskina.exception.MenuNotFoundException;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class MenuNodeService {

    private final MenuNodeRepository menuNodeRepository;

    @Inject
    public MenuNodeService(MenuNodeRepository menuNodeRepository) {
        this.menuNodeRepository = menuNodeRepository;
    }

    public MenuNode findById(UUID currentNodeId) {
        return menuNodeRepository.findById(currentNodeId)
                .orElseThrow(() -> new MenuNotFoundException("Меню с id: " + currentNodeId + ", не найдено"));
    }

    public List<MenuNode> findChildren(UUID currentNodeId) {
        return menuNodeRepository.findChildren(currentNodeId);
    }

    public MenuNode findMenuNodeById(String key) {
        return menuNodeRepository.findMenuNodeById(key).stream().findFirst()
                .orElseThrow(() -> new MenuNotFoundException("Меню с ключом: " + key + ", не найдено"));
    }

    public MenuNode getRootNode() {
        return menuNodeRepository.findByParentIsNull().stream().findFirst()
                .orElseThrow(() -> new MenuNotFoundException("Основное меню не найдено"));
    }
}
