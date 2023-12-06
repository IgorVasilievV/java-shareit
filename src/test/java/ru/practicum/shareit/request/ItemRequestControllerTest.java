package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void create_shouldReturnStatusIsOk() {
        ItemRequestDtoIn itemRequestDtoInToCreate = ItemRequestDtoIn.builder()
                .description("description")
                .build();
        ItemRequestDtoOut itemRequestDtoOut = ItemRequestDtoOut.builder().build();
        when(itemRequestService.create(1L, itemRequestDtoInToCreate))
                .thenReturn(itemRequestDtoOut);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDtoInToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemRequestDtoOut), result);
    }

    @SneakyThrows
    @Test
    void create_shouldReturnStatusIsBadRequest() {
        ItemRequestDtoIn itemRequestDtoInToCreate = ItemRequestDtoIn.builder()
                .description("description")
                .build();
        when(itemRequestService.create(1L, itemRequestDtoInToCreate))
                .thenThrow(ValidationException.class);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDtoInToCreate)))
                .andExpect(status().isBadRequest());

        verify(itemRequestService).create(anyLong(), any(ItemRequestDtoIn.class));

    }

    @SneakyThrows
    @Test
    void findItemRequestByOwner_shouldReturnStatusIsOk() {
        when(itemRequestService.findItemRequestByOwner(1L))
                .thenReturn(Collections.EMPTY_LIST);

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(Collections.EMPTY_LIST), result);
    }

    @SneakyThrows
    @Test
    void findItemRequestById_shouldReturnStatusIsOk() {
        ItemRequestDtoOut itemRequestDtoOut = ItemRequestDtoOut.builder().build();
        when(itemRequestService.findItemRequestById(1L, 1L))
                .thenReturn(itemRequestDtoOut);

        String result = mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemRequestDtoOut), result);
    }

    @SneakyThrows
    @Test
    void findAllItemRequest_shouldReturnStatusIsOk() {
        when(itemRequestService.findAllItemRequest(1L, 0, 2))
                .thenReturn(Collections.EMPTY_LIST);

        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(Collections.EMPTY_LIST), result);

    }
}