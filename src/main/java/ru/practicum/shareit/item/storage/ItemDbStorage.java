package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDbStorage extends JpaRepository<Item, Long> {

    List<Item> getItemsByOwnerIdOrderById(long ownerId);

    @Query(" select i.id from Item i " +
            "where i.available = true and (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Long> searchByText(String text);

    List<Item> findByIdIn(List<Long> ids);

}
