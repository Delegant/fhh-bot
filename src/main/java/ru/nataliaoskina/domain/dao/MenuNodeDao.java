package ru.nataliaoskina.domain.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.nataliaoskina.domain.model.MenuNode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterBeanMapper(MenuNode.class)   // Регистрируем маппер для маппинга на MenuNode
public interface MenuNodeDao {

    @SqlQuery("SELECT * FROM menu_node WHERE id = :id")
    @RegisterBeanMapper(MenuNode.class)
    Optional<MenuNode> findById(@Bind("id") UUID id);

    @SqlQuery("SELECT * FROM menu_node WHERE parent_id = :parentId ORDER BY order_index")
    @RegisterBeanMapper(MenuNode.class)
    List<MenuNode> findChildren(@Bind("parentId") UUID parentId);

    @SqlUpdate("INSERT INTO menu_node (id, key, label, callback_data, is_visible, order_index, parent_id) " +
               "VALUES (:id, :key, :label, :callbackData, :isVisible, :orderIndex, :parentId)")
    void createMenuNode(@BindBean MenuNode menuNode);

    @SqlQuery("SELECT * FROM menu_node WHERE key = :key")
    List<MenuNode> findMenuNodeById(@Bind("key") String key);

    // Получаем список всех меню, если нужно
    @SqlQuery("SELECT * FROM menu_node ORDER BY order_index")
    List<MenuNode> getAllMenuNodes();

    @SqlQuery("SELECT * FROM menu_node WHERE parent_id IS NULL")
    List<MenuNode> findByParentIsNull();
}
