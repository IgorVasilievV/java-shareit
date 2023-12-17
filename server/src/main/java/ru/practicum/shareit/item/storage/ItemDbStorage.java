package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select i.id from items as i where " +
            "i.is_available = true and" +
            "(upper(i.name COLLATE \"en_US\") like upper(concat('%', ?1, '%') COLLATE \"en_US\") or " +
            "upper(i.description COLLATE \"en_US\") like upper(concat('%', ?1, '%') COLLATE \"en_US\"))",
            nativeQuery = true)
    List<Long> searchByText(String text);

    Page<Item> findByIdInOrderById(List<Long> ids, Pageable pageRequest);

    List<Item> findAllByRequestId(Long id);

    Page<Item> getItemsByOwnerId(Long ownerId, PageRequest pageRequest);
}
