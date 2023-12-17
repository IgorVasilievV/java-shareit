package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.impl.UserServiceDbImpl;
import ru.practicum.shareit.user.storage.UserDbStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDbStorage userStorage;

    @Mock
    @Qualifier("ValidationUserServiceDbImpl")
    private ValidationUserService validationUserService;

    @InjectMocks
    private UserServiceDbImpl userService;

    @Captor
    private ArgumentCaptor<User> argumentCaptor;

    @SneakyThrows
    @Test
    void create_shouldReturnUserDto() {
        UserDto userDtoToSave = new UserDto();
        User userToReturn = User.builder().build();
        when(userStorage.save(any(User.class))).thenReturn(userToReturn);

        UserDto userDtoToReturn = userService.create(userDtoToSave);
        assertEquals(userDtoToSave, userDtoToReturn);
        verify(userStorage).save(any(User.class));
    }

    @SneakyThrows
    @Test
    void create_shouldReturnException() {
        UserDto userDtoToSave = new UserDto();
        doThrow(ValidationException.class).when(validationUserService).validateBeforeCreate(userDtoToSave);

        assertThrows(ValidationException.class, () -> userService.create(userDtoToSave));

        verify(userStorage, never()).save(any(User.class));
    }

    @SneakyThrows
    @Test
    void update_shouldReturnUserDto() {
        UserDto userDtoToUpdate = UserDto.builder()
                .id(1L)
                .name("nameNew")
                .email("emailNew@email.com")
                .build();
        User userOld = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
        when(userStorage.getReferenceById(anyLong())).thenReturn(userOld);
        when(userStorage.save(any(User.class))).thenReturn(new User());

        userService.update(1L, userDtoToUpdate);
        verify(userStorage).save(argumentCaptor.capture());
        User userUpdated = argumentCaptor.getValue();
        assertEquals(userDtoToUpdate.getName(), userUpdated.getName());
        assertEquals(userDtoToUpdate.getEmail(), userUpdated.getEmail());
    }

    @SneakyThrows
    @Test
    void update_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationUserService).validateBeforeUpdate(anyLong(), any(UserDto.class));

        assertThrows(ValidationException.class, () -> userService.update(1L, new UserDto()));

        verify(userStorage, never()).save(any(User.class));
    }

    @SneakyThrows
    @Test
    void getById_shouldReturnUserDto() {
        long id = 1L;
        User userReturned = User.builder()
                .id(id)
                .build();
        UserDto userExpected = UserDto.builder()
                .id(id)
                .build();
        when(userStorage.findById(id)).thenReturn(Optional.of(userReturned));

        UserDto actualUserDto = userService.getById(1L);
        assertEquals(userExpected, actualUserDto);
    }

    @SneakyThrows
    @Test
    void getById_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationUserService).validateSearch(anyLong());

        assertThrows(ValidationException.class, () -> userService.getById(1L));

        verify(userStorage, never()).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void getAll_shouldReturnListUserDto() {
        when(userStorage.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<UserDto> actualUserDtoList = userService.getAll();
        assertEquals(Collections.EMPTY_LIST, actualUserDtoList);
        verify(userStorage).findAll();
    }

    @SneakyThrows
    @Test
    void deleteById_shouldDeleteUser() {
        userService.deleteById(1L);
        verify(userStorage).deleteById(anyLong());
    }

    @SneakyThrows
    @Test
    void deleteById_shouldReturnException() {
        doThrow(ValidationException.class)
                .when(validationUserService).validateSearch(anyLong());

        assertThrows(ValidationException.class, () -> userService.deleteById(1L));

        verify(userStorage, never()).deleteById(anyLong());
    }
}