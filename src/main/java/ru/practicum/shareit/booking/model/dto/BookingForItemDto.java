package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingForItemDto {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private long bookerId;

}
