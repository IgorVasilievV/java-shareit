package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testEquals() {
        User user1 = User.builder().id(1L).name("name").build();
        User user2 = User.builder().id(1L).name("name1").build();
        User user3 = User.builder().id(2L).name("name").build();
        User user4 = null;

        assertTrue(user1.equals(user2));
        assertFalse(user1.equals(user3));
        assertFalse(user1.equals(user4));
    }
}