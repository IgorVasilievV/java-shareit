package ru.practicum.shareit.booking.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@Builder
public class BookingDtoWithoutEntity {
    public BookingDtoWithoutEntity(Long id, LocalDateTime start, LocalDateTime end, String status, Long bookerId, Long itemId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.bookerId = bookerId;
        this.itemId = itemId;
    }

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private String status;

    private Long bookerId;

    private Long itemId;

}
