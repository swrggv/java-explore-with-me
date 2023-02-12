package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Create;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(groups = Create.class, message = "Category name can not be empty")
    private String name;
}
