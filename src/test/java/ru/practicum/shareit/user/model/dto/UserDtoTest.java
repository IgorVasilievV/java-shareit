package ru.practicum.shareit.user.model.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    JacksonTester<UserDto> json;

    @Test
    @SneakyThrows
    void testSerialization() {
        UserDto userDto = new UserDto(1L, "name", "mail@email.ru");
        String expectedJson = "{\"id\":1,\"name\":\"name\",\"email\":\"mail@email.ru\"}";

        assertThat(json.write(userDto)).isEqualToJson(expectedJson);
    }

    @Test
    @SneakyThrows
    public void testDeserialization() {
        String jsonValue = "{\"id\":1,\"name\":\"name\",\"email\":\"mail@email.ru\"}";
        UserDto userDtoExpected = new UserDto(1L, "name", "mail@email.ru");

        assertThat(json.parse(jsonValue)).usingRecursiveComparison().isEqualTo(userDtoExpected);
    }
}