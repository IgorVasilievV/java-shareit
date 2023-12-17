package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.dto.BookingForItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private List<CommentDtoOut> comments;
}
