package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(groups = Create.class, message = "Name can not be empty")
    private String name;
    @NotBlank(groups = Create.class, message = "Email can not be empty")
    @Email(groups = Create.class, message = "Email should be correct")
    private String email;
}
