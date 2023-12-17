package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CommentStorageTest {

    @Autowired
    CommentStorage commentStorage;

    @Test
    void findByItemId() {
    }
}