package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto changeCategory(CategoryDto categoryDto);

    CategoryDto getCategory(int idCategory);

    void deleteCategory(int catId);

    List<CategoryDto> getCategoryFromSize(int from, int size);
}
