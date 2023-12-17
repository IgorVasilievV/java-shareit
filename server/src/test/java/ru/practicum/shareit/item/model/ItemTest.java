package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testEquals() {
        Item item1 = Item.builder().id(1L).description("description").build();
        Item item2 = Item.builder().id(1L).description("description1").build();
        Item item3 = Item.builder().id(2L).description("description").build();
        Item item4 = null;

        assertTrue(item1.equals(item2));
        assertFalse(item1.equals(item3));
        assertFalse(item1.equals(item4));
    }
}