package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;

import java.util.Collections;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    @Qualifier("UserServiceDbImpl")
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    @SneakyThrows
    @Test
    void getById() {
        when(userService.getById(Mockito.anyLong())).thenReturn(new UserDto());
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(userService).getById(Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void create() {
        UserDto userDtoToCreate = new UserDto();
        when(userService.create(userDtoToCreate)).thenReturn(userDtoToCreate);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(userDtoToCreate), result);
    }

    @SneakyThrows
    @Test
    void update() {
        UserDto userDtoToUpdate = new UserDto();
        userDtoToUpdate.setId(1L);
        when(userService.update(1L, userDtoToUpdate)).thenReturn(userDtoToUpdate);

        String result = mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(userDtoToUpdate), result);
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(userService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        Mockito.verify(userService).getAll();
    }

    @SneakyThrows
    @Test
    void deleteById() {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(userService).deleteById(Mockito.anyLong());
    }
}