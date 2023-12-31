package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {


    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Optional<Booking> findById(Long bookingId);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByBookerIdOrderByStartDesc(Long id);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(
            Long id, LocalDateTime dateStart);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(
            Long id, LocalDateTime dateEnd);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long id, LocalDateTime dateStart, LocalDateTime dateEnd);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long id, String status);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByItem_owner_idOrderByStartDesc(Long userId);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByItem_owner_idAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime date);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByItem_owner_idAndStartAfterOrderByStartDesc(Long userId, LocalDateTime date);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByItem_owner_idAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime dateStart, LocalDateTime dateEnd);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    List<Booking> findAllByItem_owner_idAndStatusOrderByStartDesc(Long userId, String status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByEndDesc(Item item, LocalDateTime dateStart, String status);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime dateStart, String status);

    Booking findFirstByItemIdAndBookerIdAndStatus(Long itemId, Long userId, String status);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByBookerId(Long ownerId, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByBookerIdAndEndBefore(Long ownerId, LocalDateTime now, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByBookerIdAndStartAfter(Long ownerId, LocalDateTime now, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime now, LocalDateTime now1,
                                                             PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByBookerIdAndStatus(Long ownerId, String string, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByItem_owner_id(Long ownerId, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByItem_owner_idAndEndBefore(Long ownerId, LocalDateTime now, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByItem_owner_idAndStartAfter(Long ownerId, LocalDateTime now, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByItem_owner_idAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime now, LocalDateTime now1, PageRequest pageRequest);

    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Page<Booking> findAllByItem_owner_idAndStatus(Long ownerId, String string, PageRequest pageRequest);
}

