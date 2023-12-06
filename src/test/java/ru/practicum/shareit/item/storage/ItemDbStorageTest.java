package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {"db.name=test"})
class ItemDbStorageTest {

    @Autowired
    ItemDbStorage itemStorage;

    @BeforeEach
    void addItems() {
        Item item = Item.builder()
                .available(true)
                .name("text").build();
        Item item1 = Item.builder()
                .available(false)
                .name("text").build();
        Item item2 = Item.builder()
                .available(false)
                .name("abracadabra").build();

        itemStorage.save(item);
        itemStorage.save(item1);
        itemStorage.save(item2);
    }

    @Test
    void searchByText() {
        List<Long> idsActual = itemStorage.searchByText("text");

        assertEquals(1, idsActual.size());
        assertEquals(1, idsActual.get(0));

    }
}