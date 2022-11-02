package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

@RestController
@Validated
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Validated(Create.class) NewCategoryDto newCategoryDto) {
        log.info("Category {} was added", newCategoryDto);
        return categoryService.addCategory(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto patchCategory(@RequestBody @Validated CategoryDto categoryDto) {
        log.info("{} was changed", categoryDto);
        return categoryService.changeCategory(categoryDto);
    }

    @DeleteMapping("{catId}")
    public void deleteCategory(@PathVariable int catId) {
        log.info("Get category {}", catId);
        categoryService.deleteCategory(catId);
    }
}
