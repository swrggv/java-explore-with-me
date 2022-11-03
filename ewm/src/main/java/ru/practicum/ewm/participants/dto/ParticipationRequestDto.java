package ru.practicum.ewm.participants.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.user.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    @NotNull(groups = Create.class, message = "Id event is required")
    private Long id;
    private LocalDateTime created = LocalDateTime.now();
    @NotNull(groups = Create.class, message = "Id event is required")
    private Long event;
    @NotNull(groups = Create.class, message = "Id event is required")
    private Long requester;
    private Status status = Status.PENDING;
}
