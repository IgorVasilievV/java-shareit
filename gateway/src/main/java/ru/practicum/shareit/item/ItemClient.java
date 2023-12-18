package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getById(Long id, Long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> create(Long ownerId, ItemDto itemDto) {
        return post("/", ownerId, itemDto);
    }

    public ResponseEntity<Object> update(Long ownerId, Long id, ItemDto itemDto) {
        return patch("/" + id, ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemsByUser(Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from != null && size != null) {
            parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("?from={from}&size={size}", ownerId, parameters);
        } else {
            return get("", ownerId);
        }
    }

    public ResponseEntity<Object> getItemsBySearch(Long renterId, String text, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from != null && size != null) {
            parameters = Map.of(
                    "text", text,
                    "from", from,
                    "size", size
            );
            return get("/search?text={text}&from={from}&size={size}", renterId, parameters);
        } else {
            parameters = Map.of(
                    "text", text
            );
            return get("/search?text={text}", renterId, parameters);
        }
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDtoIn commentDtoIn) {
        return post("/" + itemId + "/comment", userId, commentDtoIn);
    }
}
