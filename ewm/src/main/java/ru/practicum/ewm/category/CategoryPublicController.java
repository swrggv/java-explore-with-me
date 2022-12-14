package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Get categories from {} size {}", from, size);
        return categoryService.getCategoryFromSize(from, size);
    }

    @GetMapping("{catId}")
    public CategoryDto getCategoryById(@PathVariable int catId) {
        log.info("Get category {}", catId);
        return categoryService.getCategory(catId);
    }
}
