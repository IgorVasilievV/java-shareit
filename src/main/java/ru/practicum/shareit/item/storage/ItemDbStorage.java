package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDbStorage extends JpaRepository<Item, Long> {

    List<Item> getItemsByOwnerIdOrderById(long ownerId);

    @Query(value = "select i.id from items as i where " +
            "i.is_available = true and" +
            "(upper(i.name COLLATE \"en_US\") like upper(concat('%', ?1, '%') COLLATE \"en_US\") or " +
            "upper(i.description COLLATE \"en_US\") like upper(concat('%', ?1, '%') COLLATE \"en_US\"))",
            nativeQuery = true)
    List<Long> searchByText(String text);

    List<Item> findByIdIn(List<Long> ids);

}
