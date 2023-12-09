package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Qualifier("ItemServiceDbImpl")
    ItemService itemService;

    @SneakyThrows
    @Test
    void getById() {
        ItemWithBookingDto itemWithBookingDto = ItemWithBookingDto.builder().build();
        when(itemService.getById(1L, 1L))
                .thenReturn(itemWithBookingDto);

        String result = mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemWithBookingDto), result);
    }

    @SneakyThrows
    @Test
    void create() {
        ItemDto itemDtoToCreate = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        when(itemService.create(1L, itemDtoToCreate))
                .thenReturn(itemDtoToCreate);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemDtoToCreate), result);
    }

    @SneakyThrows
    @Test
    void update() {
        ItemDto itemDtoToUpdate = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        when(itemService.update(1L, 1L, itemDtoToUpdate))
                .thenReturn(itemDtoToUpdate);

        String result = mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemDtoToUpdate), result);
    }

    @SneakyThrows
    @Test
    void getItemsByUser() {
        when(itemService.getItemsByUser(1L, 0, 2))
                .thenReturn(Collections.EMPTY_LIST);

        String result = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(Collections.EMPTY_LIST), result);
    }

    @SneakyThrows
    @Test
    void getItemsBySearch() {
        when(itemService.getItemsBySearch(1L, "text", 0, 2))
                .thenReturn(Collections.EMPTY_LIST);

        String result = mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "text")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(Collections.EMPTY_LIST), result);
    }

    @SneakyThrows
    @Test
    void createComment() {
        CommentDtoIn commentDtoIn = CommentDtoIn.builder()
                .text("text")
                .build();
        CommentDtoOut commentDtoOut = CommentDtoOut.builder().build();
        when(itemService.createComment(1L, 1L, commentDtoIn))
                .thenReturn(commentDtoOut);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDtoIn)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(commentDtoOut), result);
    }
}