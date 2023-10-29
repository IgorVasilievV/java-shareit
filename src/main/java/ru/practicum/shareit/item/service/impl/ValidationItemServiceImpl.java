package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.SecurityException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.service.ValidationItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class ValidationItemServiceImpl implements ValidationItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public void validateBeforeCreate(long ownerId, ItemDto itemDto) throws NotFoundException, ValidationException {
        validateWithoutOwner(ownerId);
        validateContainsOwner(ownerId);
        validateData(itemDto);
    }

    @Override
    public void validateBeforeUpdate(long ownerId, long id, ItemDto itemDto)
            throws NotFoundException, ValidationException, SecurityException {
        validateSearch(id);
        validateWithoutOwner(ownerId);
        validateContainsOwner(ownerId);
        validateSecurity(ownerId, id);
    }

    @Override
    public void validateSearch(long id) throws NotFoundException {
        if (itemStorage.getAll().stream().noneMatch(i -> i.getId() == id)) {
            throw new NotFoundException("Item with id = " + id + " not found");
        }
    }

    @Override
    public void validateSearchByUser(long ownerId) {
        validateWithoutOwner(ownerId);
        validateContainsOwner(ownerId);
    }

    private void validateContainsOwner(long ownerId) throws NotFoundException {
        if (userStorage.getAll().stream().noneMatch(u -> u.getId() == ownerId)) {
            throw new NotFoundException("Unknown user with id = " + ownerId);
        }
    }

    private void validateWithoutOwner(long ownerId) throws ValidationException {
        if (ownerId == -1) {
            throw new ValidationException("Item without owner");
        }
    }

    private void validateData(ItemDto itemDto) throws ValidationException {
        if (itemDto.getName() == null || itemDto.getName().isBlank() ||
                itemDto.getDescription() == null || itemDto.getDescription().isBlank() ||
                itemDto.getAvailable() == null) {
            throw new ValidationException("Item without allNecessaryData");
        }
    }

    private void validateSecurity(long ownerId, long id) throws SecurityException {
        if (itemStorage.getById(id).getOwner().getId() != ownerId) {
            throw new SecurityException("User don't owner");
        }
    }
}
