package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.dto.BookingDtoWithoutEntity;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {


    @EntityGraph(value = "booking-entity-graph-with-item-and-booker")
    Optional<Booking> findById(Long bookingId);

    List<BookingDtoWithoutEntity> findAllByBookerIdOrderByStartDesc(Long id);

    List<BookingDtoWithoutEntity> findAllByBookerIdAndStartAfterOrderByStartDesc(
            Long id, LocalDateTime dateStart);

    List<BookingDtoWithoutEntity> findAllByBookerIdAndEndBeforeOrderByStartDesc(
            Long id, LocalDateTime dateEnd);

    List<BookingDtoWithoutEntity> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long id, LocalDateTime dateStart, LocalDateTime dateEnd);

    List<BookingDtoWithoutEntity> findAllByBookerIdAndStatusOrderByStartDesc(Long id, String status);

    @Query("select b from Booking as b join fetch b.item as i join fetch i.owner as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    List<Booking> findBookingItemByUser(Long userId);

    @Query("select b from Booking as b join fetch b.item as i join fetch i.owner as u " +
            "where u.id = ?1 and " +
            "b.end < ?2 " +
            "order by b.start desc")
    List<Booking> findBookingItemByUserPast(Long userId, LocalDateTime date);

    @Query("select b from Booking as b join fetch b.item as i join fetch i.owner as u " +
            "where u.id = ?1 and " +
            "b.start > ?2 " +
            "order by b.start desc")
    List<Booking> findBookingItemByUserFuture(Long userId, LocalDateTime date);

    @Query("select b from Booking as b join fetch b.item as i join fetch i.owner as u " +
            "where u.id = ?1 and " +
            "b.start < ?2 and " +
            "b.end > ?3 " +
            "order by b.start desc")
    List<Booking> findBookingItemByUserCurrent(Long userId, LocalDateTime dateStart, LocalDateTime dateEnd);

    @Query("select b from Booking as b join fetch b.item as i join fetch i.owner as u " +
            "where u.id = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findBookingItemByUserStatus(Long userId, String status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByEndDesc(Item item, LocalDateTime dateStart, String status);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime dateStart, String status);

    Booking findFirstByItemIdAndBookerIdAndStatus(Long itemId, Long userId, String status);
}
