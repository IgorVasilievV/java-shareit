package ru.practicum.shareit.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.ValidationUserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service("UserServiceImpl")
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final ValidationUserService validationUserService;

    @Autowired
    public UserServiceImpl(UserStorage userStorage,
                           @Qualifier("ValidationUserServiceImpl") ValidationUserService validationUserService) {
        this.userStorage = userStorage;
        this.validationUserService = validationUserService;
    }

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
    public UserDto update(Long id, UserDto userDto) {
        validationUserService.validateBeforeUpdate(id, userDto);
        User userFromMapper = UserMapper.toUser(userDto);
        User user = userStorage.update(id, userFromMapper);
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Updated user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    public UserDto getById(Long id) {
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
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        validationUserService.validateSearch(id);
        userStorage.deleteById(id);
        log.info("Deleted user id = {}", id);
    }
}
