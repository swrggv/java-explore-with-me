package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    private NewUserRequest newUserRequest;

    @BeforeEach
    void prepare() {
        newUserRequest = new NewUserRequest("user", "user@gmail.com");
    }

    @Test
    void addUser() {
        UserDto userDto = userService.addUser(newUserRequest);
        assertThat(userDto.getName()).isEqualTo(newUserRequest.getName());
        assertThat(userDto.getEmail()).isEqualTo(newUserRequest.getEmail());
    }

    @Test
    void getAllUsers() {
        UserDto userDto = userService.addUser(newUserRequest);
        UserDto userDto2 = userService.addUser(new NewUserRequest("user2", "email2@gmail.com"));
        assertThat(userService.getUsers(List.of(userDto.getId(), userDto2.getId()), 0, 10)).hasSize(2);
    }

    @Test
    void getAllUsersByOneId() {
        UserDto userDto = userService.addUser(newUserRequest);
        userService.addUser(new NewUserRequest("user2", "email2@gmail.com"));
        assertThat(userService.getUsers(List.of(userDto.getId()), 0, 10)).hasSize(1);
    }
}