package ru.practicum.main.service.category.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.category.dto.NewCategoryDto;
import ru.practicum.main.service.category.dto.mapper.CategoryMapper;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.api.CategoryRepository;
import ru.practicum.main.service.exception.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    private final Long categoryId = 1L;
    private final String name = "categoryName";

    private final NewCategoryDto newCategoryDto = new NewCategoryDto(name);
    private final Category newCategory = new Category(null, name);
    private final Category addedCategory = new Category(categoryId, name);
    private final CategoryDto categoryDto = new CategoryDto(categoryId, name);
    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void addCategory() {
        when(categoryMapper.toCategory(newCategoryDto)).thenReturn(newCategory);
        when(categoryRepository.save(newCategory)).thenReturn(addedCategory);
        when(categoryMapper.toCategoryDto(addedCategory)).thenReturn(categoryDto);

        CategoryDto resultCategoryDto = categoryService.addCategoryAdmin(newCategoryDto);

        InOrder inOrder = inOrder(categoryMapper, categoryRepository);

        assertAll(
                () -> inOrder.verify(categoryMapper).toCategory(newCategoryDto),
                () -> inOrder.verify(categoryRepository).save(newCategory),
                () -> inOrder.verify(categoryMapper).toCategoryDto(addedCategory),
                () -> assertEquals(addedCategory.getId(), resultCategoryDto.getId()),
                () -> assertEquals(newCategoryDto.getName(), resultCategoryDto.getName())
        );
    }

    @Test
    void updateCategory_whenCategoryFoundAndNewName_thenUpdateName() {
        CategoryDto newCategoryDto = new CategoryDto(categoryId + 1, "new" + name);
        Category oldCategory = new Category(categoryId, name);
        Category newCategory = new Category(
                newCategoryDto.getId(),
                newCategoryDto.getName()
        );
        Category categoryAfter = new Category(
                oldCategory.getId(),
                newCategory.getName()
        );
        CategoryDto categoryDtoAfter = new CategoryDto(
                categoryAfter.getId(),
                categoryAfter.getName()
        );
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(oldCategory));
        when(categoryMapper.toCategory(newCategoryDto)).thenReturn(newCategory);
        when(categoryRepository.save(newCategory)).thenReturn(categoryAfter);
        when(categoryMapper.toCategoryDto(categoryAfter)).thenReturn(categoryDtoAfter);

        CategoryDto returnedCategoryDto = categoryService.updateCategoryAdmin(categoryId, newCategoryDto);

        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category categoryForSave = categoryArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(categoryDtoAfter, returnedCategoryDto),
                () -> assertNotEquals(newCategoryDto.getId(), categoryForSave.getId()),
                () -> assertEquals(oldCategory.getId(), categoryForSave.getId()),
                () -> assertEquals(newCategoryDto.getName(), categoryForSave.getName()),
                () -> verify(categoryRepository).findById(categoryId),
                () -> verify(categoryMapper).toCategory(newCategoryDto),
                () -> verify(categoryMapper).toCategoryDto(categoryAfter)
        );
    }

    @Test
    void updateCategory_whenCategoryNotFound_thenNotFoundException() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.getCategoryByIdPub(categoryId));
    }

    @Test
    void deleteCategoryById() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(newCategory));

        categoryService.deleteCategoryByIdAdmin(categoryId);
        verify(categoryRepository).deleteById(longArgumentCaptor.capture());
        Long idForDelete = longArgumentCaptor.getValue();
        assertEquals(categoryId, idForDelete);
    }

    @Test
    void deleteCategoryById_whenCategoryNotFound_thenNotFoundException() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategoryByIdAdmin(categoryId));
    }

    @Test
    void getAllCategories() {

    }

    @Test
    void getCategoryById() {

    }

    @Test
    void getCategoryById_whenCategoryNotFound_thenNotFoundException() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.getCategoryByIdPub(categoryId));
    }
}