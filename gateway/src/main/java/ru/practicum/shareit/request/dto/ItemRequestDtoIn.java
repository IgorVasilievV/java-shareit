package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoIn {
    @NotNull
    @NotBlank
    private String description;
}
