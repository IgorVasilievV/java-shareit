package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Long ownerId, Item item);

    Item update(Long id, Item item);

    Item getById(Long id);

    List<Item> getAll();

    List<Item> getItemsByUser(Long ownerId);

    List<Item> getItemsBySearch(String text);

    void deleteById(Long id);
}
