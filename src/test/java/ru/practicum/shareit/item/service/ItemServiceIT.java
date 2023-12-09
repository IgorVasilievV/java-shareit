package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.impl.ItemServiceDbImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIT {

    private final ItemServiceDbImpl itemService;

    private final UserDbStorage userStorage;

    @Test
    void getItemsByUser() {
        userStorage.save(User.builder()
                .name("nameUser")
                .email("email@ru")
                .build());

        itemService.create(1L, ItemDto.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available(true)
                .build());

        List<ItemWithBookingDto> itemsByUserActual = itemService.getItemsByUser(1L, 0, 2);
        assertEquals(1, itemsByUserActual.size());
        assertEquals(1, itemsByUserActual.get(0).getId());
    }
}