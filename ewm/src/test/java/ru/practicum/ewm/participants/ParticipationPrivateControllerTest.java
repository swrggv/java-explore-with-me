package ru.practicum.ewm.participants;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.participants.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.Status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ParticipationPrivateController.class)
class ParticipationPrivateControllerTest {
    @MockBean
    private ParticipationService participationService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;
    private ParticipationRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = ParticipationRequestDto.builder()
                .id(1L)
                .event(1L)
                .requestor(1L)
                .status(Status.CONFIRMED)
                .build();
    }

    @Test
    void getRequests() throws Exception {
        Mockito.when(participationService.getAllRequestsForUser(anyLong())).thenReturn(List.of(requestDto));
        mvc.perform(get("/users/{userId}/requests", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$[0].requestor", is(requestDto.getRequestor()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.CONFIRMED))));
    }

    @Test
    void addRequest() throws Exception {
        Mockito.when(participationService.addRequest(anyLong(), anyLong())).thenReturn(requestDto);
        mvc.perform(post("/users/{userId}/requests", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                        .param("eventId", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$.requestor", is(requestDto.getRequestor()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.CONFIRMED))));
    }

    @Test
    void rejectRequest() throws Exception {
        Mockito.when(participationService.rejectRequestFromOwnerRequest(anyLong(), anyLong())).thenReturn(requestDto);
        mvc.perform(patch("/users/{userId}/requests/{requestId}/cancel", 1L, 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.event", is(requestDto.getEvent()), Long.class))
                .andExpect(jsonPath("$.requestor", is(requestDto.getRequestor()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.CONFIRMED))));
    }
}