package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BookingService bookingService;

    @SneakyThrows
    @Test
    void create() {
        BookingDtoIn bookingDtoInToCreate = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusMinutes(30))
                .end(LocalDateTime.now().plusHours(1))
                .itemId(1L)
                .build();
        BookingDtoOut bookingDtoOut = new BookingDtoOut();
        when(bookingService.create(1L, bookingDtoInToCreate))
                .thenReturn(bookingDtoOut);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoInToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @SneakyThrows
    @Test
    void setStatusBooking() {
        BookingDtoOut bookingDtoOut = new BookingDtoOut();
        when(bookingService.setStatusBooking(1L, 1L, true))
                .thenReturn(bookingDtoOut);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @SneakyThrows
    @Test
    void findBookingById() {
        BookingDtoOut bookingDtoOut = new BookingDtoOut();
        when(bookingService.findBookingById(1L, 1L))
                .thenReturn(bookingDtoOut);

        String result = mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @SneakyThrows
    @Test
    void findBookingByUser() {
        BookingDtoOut bookingDtoOut = new BookingDtoOut();
        when(bookingService.findBookingByUser(1L, "ALL", 0, 2))
                .thenReturn(Collections.EMPTY_LIST);

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
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
    void findBookingAllItemsByUser() {
        BookingDtoOut bookingDtoOut = new BookingDtoOut();
        when(bookingService.findBookingAllItemsByUser(1L, "ALL", 0, 2))
                .thenReturn(Collections.EMPTY_LIST);

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(Collections.EMPTY_LIST), result);
    }
}