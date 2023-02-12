package ru.practicum.ewm.category;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.category.dto.CategoryDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryPublicController.class)
class CategoryPublicControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mvc;

    private final CategoryDto categoryDto = new CategoryDto(1, "category");

    @Test
    void getCategories() throws Exception {
        Mockito.when(categoryService.getCategoryFromSize(anyInt(), anyInt())).thenReturn(List.of(categoryDto));
        mvc.perform(get("/categories")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(categoryDto.getId())))
                .andExpect(jsonPath("$[0].name", is(categoryDto.getName())));
    }

    @Test
    void getCategoryById() throws Exception {
        Mockito.when(categoryService.getCategory(anyInt())).thenReturn(categoryDto);
        mvc.perform(get("/categories/{catId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId())))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }
}