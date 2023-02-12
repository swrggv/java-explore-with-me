package ru.practicum.ewm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAdminController.class)
class UserAdminControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private UserDto userDto;
    private NewUserRequest newUserRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "user", "user@gmail.com");
        newUserRequest = new NewUserRequest("user", "user@gmail.com");
    }

    @Test
    void getUsers() throws Exception {
        Mockito.when(userService.getUsers(any(), anyInt(), anyInt())).thenReturn(List.of(userDto));
        mvc.perform(get("/admin/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", "0")
                        .param("to", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test
    void addUser() throws Exception {
        Mockito.when(userService.addUser(any())).thenReturn(userDto);
        mvc.perform(post("/admin/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(newUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/admin/users/{userId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}