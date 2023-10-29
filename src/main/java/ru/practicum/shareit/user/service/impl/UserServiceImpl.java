package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.ValidationUserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final ValidationUserService validationUserService;

    @Override
    public UserDto create(UserDto userDto) {
        validationUserService.validateBeforeCreate(userDto);
        User userFromMapper = UserMapper.toUser(userDto);
        User user = userStorage.create(userFromMapper);
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Created user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        validationUserService.validateBeforeUpdate(id, userDto);
        User userFromMapper = UserMapper.toUser(userDto);
        User user = userStorage.update(id, userFromMapper);
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Updated user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    public UserDto getById(long id) {
        validationUserService.validateSearch(id);
        User user = userStorage.getById(id);
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Found user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Found all users");
        return userStorage.getAll().stream()
                .map(u -> UserMapper.toUserDto(u))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        validationUserService.validateSearch(id);
        userStorage.deleteById(id);
        log.info("Deleted user id = {}", id);
    }
}
