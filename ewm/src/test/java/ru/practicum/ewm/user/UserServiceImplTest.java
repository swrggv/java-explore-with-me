package ru.practicum.ewm.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    private NewUserRequest newUserRequest;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        newUserRequest = new NewUserRequest("user", "user@gmail.com");
        userDto = new UserDto(1, "user", "user@gmail.com");
    }

    @Test
    void addUser() {
        UserDto result = userService.addUser(newUserRequest);
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    void getAllUsers() {
        UserDto userDto = userService.addUser(newUserRequest);
        UserDto userDto2 = userService.addUser(new NewUserRequest("user2", "email2@gmail.com"));
        assertThat(userService.getUsers(List.of(userDto.getId(), userDto2.getId()), 0, 10)).hasSize(2);
    }

    @Test
    void getUserById() {
        userService.addUser(newUserRequest);
        assertThat(userService.getUsers(List.of(userDto.getId()), 0, 10)).hasSize(1);
    }
}