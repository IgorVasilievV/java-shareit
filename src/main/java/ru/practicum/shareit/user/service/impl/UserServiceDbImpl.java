package ru.practicum.shareit.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.ValidationUserService;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service("UserServiceDbImpl")
@Transactional(readOnly = true)
@Slf4j
public class UserServiceDbImpl implements UserService {
    private final UserDbStorage userStorage;
    private final ValidationUserService validationUserService;

    @Autowired
    public UserServiceDbImpl(UserDbStorage userStorage,
                             @Qualifier("ValidationUserServiceDbImpl") ValidationUserService validationUserService) {
        this.userStorage = userStorage;
        this.validationUserService = validationUserService;
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        validationUserService.validateBeforeCreate(userDto);
        User userFromMapper = UserMapper.toUser(userDto);
        User user = userStorage.save(userFromMapper);
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Created user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        validationUserService.validateBeforeUpdate(id, userDto);
        User userFromMapper = UserMapper.toUser(userDto);
        User lastUser = userStorage.getReferenceById(id);
        if (userFromMapper.getName() != null) {
            lastUser.setName(userFromMapper.getName());
        }
        if (userFromMapper.getEmail() != null) {
            lastUser.setEmail(userFromMapper.getEmail());
        }
        User user = userStorage.save(lastUser);
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Updated user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    public UserDto getById(Long id) {
        validationUserService.validateSearch(id);
        User user = userStorage.findById(id).get();
        UserDto userDtoFromMapper = UserMapper.toUserDto(user);
        log.info("Found user id = {}", userDtoFromMapper.getId());
        return userDtoFromMapper;
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Found all users");
        return userStorage.findAll().stream()
                .map(u -> UserMapper.toUserDto(u))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        validationUserService.validateSearch(id);
        userStorage.deleteById(id);
        log.info("Deleted user id = {}", id);
    }
}
