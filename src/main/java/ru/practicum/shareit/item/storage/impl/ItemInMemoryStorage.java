package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemInMemoryStorage implements ItemStorage {

    private final UserStorage userStorage;

    private Map<Long, Item> items = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public Item create(Long ownerId, Item item) {
        item.setId(getId());
        item.setOwner(userStorage.getById(ownerId));
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long id, Item item) {
        Item lastItem = getById(id);
        if (item.getName() != null) {
            lastItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            lastItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            lastItem.setAvailable(item.getAvailable());
        }
        return lastItem;
    }

    @Override
    public Item getById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemsByUser(Long ownerId) {
        return getAll().stream()
                .filter(i -> i.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            Pattern pattern = Pattern.compile(".*" + text + ".*", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            return getAll().stream()
                    .filter(i -> {
                        Matcher matcherInName = pattern.matcher(i.getName());
                        Matcher matcherInDescription = pattern.matcher(i.getDescription());
                        return (matcherInName.find() || matcherInDescription.find()) && i.getAvailable();
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }

    private long getId() {
        return ++lastId;
    }
}
