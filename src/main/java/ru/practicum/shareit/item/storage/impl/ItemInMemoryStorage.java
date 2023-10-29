package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemInMemoryStorage implements ItemStorage {

    private final UserStorage userStorage;

    private List<Item> items = new ArrayList<>();
    private long lastId = 0;

    @Override
    public Item create(long ownerId, Item item) {
        item.setId(getId());
        item.setOwner(userStorage.getById(ownerId));
        items.add(item);
        return item;
    }

    @Override
    public Item update(long id, Item item) {
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
    public Item getById(long id) {
        return items.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Item> getAll() {
        return items;
    }

    @Override
    public List<Item> getItemsByUser(long ownerId) {
        return getAll().stream()
                .filter(i -> i.getOwner().getId() == ownerId)
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
    public void deleteById(long id) {
        items.removeIf(i -> i.getId() == id);
    }

    private long getId() {
        return ++lastId;
    }
}
