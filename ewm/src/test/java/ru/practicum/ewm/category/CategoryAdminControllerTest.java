package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryAdminController.class)
class CategoryAdminControllerTest {
    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private NewCategoryDto newCategoryDto;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        newCategoryDto = new NewCategoryDto("category");
        categoryDto = new CategoryDto(1, "category");
    }

    @Test
    void addCategory() throws Exception {
        Mockito.when(categoryService.addCategory(any())).thenReturn(categoryDto);
        mvc.perform(post("/admin/categories")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

    }

    @Test
    void patchCategory() throws Exception {
        Mockito.when(categoryService.changeCategory(any())).thenReturn(categoryDto);
        mvc.perform(patch("/admin/categories")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @Test
    void deleteCategory() throws Exception {
        mvc.perform(delete("/admin/categories/{catId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}