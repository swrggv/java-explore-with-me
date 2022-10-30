package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.exception.ModelAlreadyExistException;
import ru.practicum.ewm.exception.ModelNotFoundException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
class CategoryServiceImplTest {
    @Autowired
    private CategoryService categoryService;
    private NewCategoryDto newCategoryDto;

    @BeforeEach
    void prepare() {
        newCategoryDto = new NewCategoryDto("category");
    }

    @Test
    void addCategory() {
        CategoryDto categoryDto = categoryService.addCategory(newCategoryDto);
        assertThat(categoryDto.getName()).isEqualTo(newCategoryDto.getName());
    }

    @Test
    void changeCategory() {
        CategoryDto oldCategory = categoryService.addCategory(newCategoryDto);
        CategoryDto newCategory = new CategoryDto(oldCategory.getId(), "patched");
        CategoryDto result = categoryService.changeCategory(newCategory);
        assertThat(result).isEqualTo(newCategory);
    }

    @Test
    void changeNotExistingCategory() {
        CategoryDto categoryDto = new CategoryDto(-1, "category");
        ModelNotFoundException ex = assertThrows(ModelNotFoundException.class,
                () -> categoryService.changeCategory(categoryDto));
        assertThat(ex.getMessage().contains("Category not found"));
    }

    @Test
    void deleteCategoryById() {
        CategoryDto categoryDto = categoryService.addCategory(newCategoryDto);
        categoryService.deleteCategory(categoryDto.getId());
        ModelNotFoundException ex = assertThrows(ModelNotFoundException.class,
                () -> categoryService.getCategory(categoryDto.getId()));
        assertThat(ex.getMessage()).contains("Can not get category");
    }

    @Test
    void failCreateCategoryWithNonUniqueName() {
        categoryService.addCategory(newCategoryDto);
        NewCategoryDto newCategory = new NewCategoryDto("category");
        ModelAlreadyExistException ex = assertThrows(ModelAlreadyExistException.class,
                () -> categoryService.addCategory(newCategory));
        assertThat(ex.getMessage().contains("Can not create category"));
    }

    @Test
    void getCategoryById() {
        CategoryDto categoryDto = categoryService.addCategory(newCategoryDto);
        CategoryDto result = categoryService.getCategory(categoryDto.getId());
        assertThat(result).isEqualTo(categoryDto);
    }

    @Test
    void getCategoriesWithPagination() {
        NewCategoryDto anotherNewCategoryDto = new NewCategoryDto("Two");
        categoryService.addCategory(newCategoryDto);
        categoryService.addCategory(anotherNewCategoryDto);
        assertThat(categoryService.getCategoryFromSize(0, 2)).hasSize(2);
    }
}