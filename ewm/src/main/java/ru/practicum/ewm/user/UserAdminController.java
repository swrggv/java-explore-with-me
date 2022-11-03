package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Get all users by criteria");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Validated(Create.class) NewUserRequest userRequest) {
        log.info("User was added {}", userRequest);
        return userService.addUser(userRequest);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("User {} was deleted", userId);
        userService.deleteUser(userId);
    }
}
