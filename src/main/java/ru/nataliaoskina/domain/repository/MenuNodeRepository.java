package ru.nataliaoskina.domain.repository;

import org.jdbi.v3.core.Jdbi;
import ru.nataliaoskina.domain.dao.MenuNodeDao;
import ru.nataliaoskina.domain.model.MenuNode;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MenuNodeRepository {
    private final MenuNodeDao dao;

    @Inject
    public MenuNodeRepository(Jdbi jdbi) {
        this.dao = jdbi.onDemand(MenuNodeDao.class);
    }

    public Optional<MenuNode> findById(UUID id) {
        return dao.findById(id);
    }

    public List<MenuNode> findChildren(UUID parentId) {
        return dao.findChildren(parentId);
    }

    public List<MenuNode> findByParentIsNull() {
        return dao.findByParentIsNull();
    }

    public List<MenuNode> findMenuNodeById(String key) {
        return dao.findMenuNodeById(key);
    }
}
