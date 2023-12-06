package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {"db.name=test"})
class ItemRequestStorageTest {

    @Autowired
    ItemRequestStorage itemRequestStorage;

    @Autowired
    UserDbStorage userStorage;


    @BeforeEach
    void addRequest() {
        if (userStorage.findAll().isEmpty() || itemRequestStorage.findAll().isEmpty()) {
            User user = User.builder().id(1L).build();
            User user1 = User.builder().id(2L).build();

            User userDb = userStorage.save(user);
            User userDb1 = userStorage.save(user1);

            ru.practicum.shareit.request.model.ItemRequest request = ru.practicum.shareit.request.model.ItemRequest.builder().id(1L).requester(userDb).build();
            ru.practicum.shareit.request.model.ItemRequest request1 = ru.practicum.shareit.request.model.ItemRequest.builder().id(2L).requester(userDb1).created(LocalDateTime.now().minusHours(1)).build();
            ru.practicum.shareit.request.model.ItemRequest request2 = ru.practicum.shareit.request.model.ItemRequest.builder().id(3L).requester(userDb1).created(LocalDateTime.now()).build();

            itemRequestStorage.save(request);
            itemRequestStorage.save(request1);
            itemRequestStorage.save(request2);
        }
    }

    @Test
    @Rollback(value = false)
    void findAllByRequesterId() {
        List<ru.practicum.shareit.request.model.ItemRequest> itemRequestsActual = itemRequestStorage.findAllByRequesterId(1L);

        assertEquals(1, itemRequestsActual.size());
        assertEquals(1, itemRequestsActual.get(0).getRequester().getId());
    }

    @Test
    @Rollback(value = false)
    void findAllByRequesterIdNotOrderByCreatedDesc() {
        List<ru.practicum.shareit.request.model.ItemRequest> itemRequestsActual = itemRequestStorage.findAllByRequesterIdNotOrderByCreatedDesc(1L);

        assertEquals(2, itemRequestsActual.size());
        assertEquals(3, itemRequestsActual.get(0).getId());
    }

    @Test
    @Rollback(value = false)
    void findAllByRequesterIdNot() {
        Sort sort = Sort.by(("created")).descending();
        PageRequest pageRequest = PageRequest.of(1, 1, sort);
        Page<ru.practicum.shareit.request.model.ItemRequest> itemRequestsActual = itemRequestStorage.findAllByRequesterIdNot(1L, pageRequest);

        assertEquals(1, itemRequestsActual.toList().size());
        assertEquals(2, itemRequestsActual.toList().get(0).getId());
    }
}