package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.model.dto.BookingForItemDto;
import ru.practicum.shareit.item.model.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemForRequestDto;
import ru.practicum.shareit.item.model.dto.ItemWithBookingDto;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        Long requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .build();

    }

    public static ItemWithBookingDto toItemWithBookingDto(
            Item item, BookingForItemDto lastBooking, BookingForItemDto nextBooking, List<CommentDtoOut> comments) {
        return ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemForRequestDto toItemForRequestDto(Item item) {
        return ItemForRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest().getRequester().getId())
                .build();
    }

    public static List<ItemForRequestDto> toListItemForRequestDto(List<Item> items) {
        return items.stream().map(ItemMapper::toItemForRequestDto).collect(Collectors.toList());
    }
}
