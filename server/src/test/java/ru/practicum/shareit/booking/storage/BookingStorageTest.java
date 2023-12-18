package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {"db.name=test"})
class BookingStorageTest {

    @Autowired
    BookingStorage bookingStorage;

    @Test
    void findById() {
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByItem_owner_idOrderByStartDesc() {
    }

    @Test
    void findAllByItem_owner_idAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItem_owner_idAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItem_owner_idAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItem_owner_idAndStatusOrderByStartDesc() {
    }

    @Test
    void findFirstByItemAndStartBeforeAndStatusOrderByEndDesc() {
    }

    @Test
    void findFirstByItemAndStartAfterAndStatusOrderByStart() {
    }

    @Test
    void findFirstByItemIdAndBookerIdAndStatus() {
    }

    @Test
    void findAllByBookerId() {
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter() {
    }

    @Test
    void findAllByBookerIdAndStatus() {
    }

    @Test
    void findAllByItem_owner_id() {
    }

    @Test
    void findAllByItem_owner_idAndEndBefore() {
    }

    @Test
    void findAllByItem_owner_idAndStartAfter() {
    }

    @Test
    void findAllByItem_owner_idAndStartBeforeAndEndAfter() {
    }

    @Test
    void findAllByItem_owner_idAndStatus() {
    }
}