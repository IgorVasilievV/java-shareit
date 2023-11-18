package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long id);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(
            Long id, LocalDateTime dateStart);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(
            Long id, LocalDateTime dateEnd);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long id, LocalDateTime dateStart, LocalDateTime dateEnd);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long id, String status);

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

    Booking findFirstByItemAndStartBeforeAndStatusOrderByEndDesc(Item item, LocalDateTime dateStart, String Status);
    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime dateStart, String Status);
    Booking findFirstByItemIdAndBookerIdAndStatus(Long itemId, Long userId, String status);
}
