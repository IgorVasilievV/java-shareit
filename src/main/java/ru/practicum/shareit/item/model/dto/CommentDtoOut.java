package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoOut {
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
