package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ModelAlreadyExistException;
import ru.practicum.ewm.exception.ModelNotFoundException;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        if (checkNameExist(newCategoryDto.getName())) {
            throw new ModelAlreadyExistException("Can not create category",
                    String.format("Category %s already exist", newCategoryDto.getName()));
        } else {
            Category category = CategoryMapper.toCategoryFromNewCategoryDto(newCategoryDto);
            category = categoryRepository.save(category);
            return CategoryMapper.toCategoryDtoFromCategory(category);
        }
    }

    @Override
    @Transactional
    public CategoryDto changeCategory(CategoryDto categoryDto) {
        if (isExistCategoryById(categoryDto.getId())) {
            Category newCategory = CategoryMapper.toCategoryFromCategoryDto(categoryDto);
            Category oldCategory = CategoryMapper.toCategoryFromCategoryDto(getCategory(categoryDto.getId()));
            Category result = patch(oldCategory, newCategory);
            return CategoryMapper.toCategoryDtoFromCategory(categoryRepository.save(result));
        } else {
            throw new BadRequestException("Category not found",
                    String.format("Category %d not found", categoryDto.getId()));
        }
    }

    @Override
    public CategoryDto getCategory(int idCategory) {
        Category category = categoryRepository.findById(idCategory).orElseThrow(
                () -> new ModelNotFoundException("Impossible find category",
                        String.format("Category %d not found", idCategory)));
        return CategoryMapper.toCategoryDtoFromCategory(category);
    }

    @Override
    @Transactional
    public void deleteCategory(int catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategoryFromSize(int from, int size) {
        int page = pageNumber(from, size);
        List<Category> categories = categoryRepository.findAll(PageRequest.of(page, size)).getContent();
        return CategoryMapper.toCategoryDtoListFromCategory(categories);
    }

    private int pageNumber(int from, int size) {
        return from / size;
    }

    private boolean checkNameExist(String name) {
        return categoryRepository.existsByName(name);
    }

    private boolean isExistCategoryById(int id) {
        return categoryRepository.existsById(id);
    }

    private Category patch(Category oldCategory, Category newCategory) {
        if (newCategory.getName() != null) {
            oldCategory.setName(newCategory.getName());
        }
        return oldCategory;
    }
}
