package ru.practicum.ewm.category.dto;

import org.mapstruct.Mapper;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);
    CategoryDto toCategoryDto(Category category);
    Category toCategory(CategoryDto categoryDto);
    List<CategoryDto> toCategoryDtoList(List<Category> categories);
}
