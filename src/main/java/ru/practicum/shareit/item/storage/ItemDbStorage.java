package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDbStorage extends JpaRepository<Item, Long> {

    List<Item> getItemsByOwnerIdOrderById(long ownerId);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Item> findById(Long itemId);

    @Query(" select i.id from Item i " +
            "where i.available = true and (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Long> searchByText(String text);

    List<Item> findByIdIn(List<Long> ids);

    Page<Item> findByIdIn(List<Long> ids, PageRequest pageRequest);

    List<Item> findAllByRequestId(Long id);

    Page<Item> getItemsByOwnerId(Long ownerId, PageRequest pageRequest);
}
