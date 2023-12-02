package ru.practicum.shareit.request.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.dto.ItemForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequestDtoOut {
    private Long id;
    private String description;
    private List<ItemForRequestDto> items;
    private LocalDateTime created;
}
