package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(long ownerId, Item item);

    Item update(long id, Item item);

    Item getById(long id);

    List<Item> getAll();

    List<Item> getItemsByUser(long ownerId);

    List<Item> getItemsBySearch(String text);

    void deleteById(long id);
}
