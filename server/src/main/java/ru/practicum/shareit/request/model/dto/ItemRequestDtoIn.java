package ru.practicum.shareit.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoIn {
    private String description;
}
