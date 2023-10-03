package ru.practicum.main.service.category.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.category.dto.NewCategoryDto;
import ru.practicum.main.service.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryDto> addCategoryAdmin(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("CategoryController: receive POST request for add new category with body={}", newCategoryDto);
        CategoryDto savedCategory = categoryService.addCategoryAdmin(newCategoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategoryAdmin(@RequestBody @Valid CategoryDto categoryDto,
                                                      @PathVariable(value = "catId") @Positive Long categoryId) {
        log.info("receive PATCH request for update category with id={}, requestBody={}", categoryId, categoryDto);
        CategoryDto updatedCategoryDto = categoryService.updateCategoryAdmin(categoryId, categoryDto);
        return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<HttpStatus> deleteCategoryByIdAdmin(@PathVariable("catId") @Positive Long categoryId) {
        log.info("receive DELETE request to delete category with categoryId= {}", categoryId);
        categoryService.deleteCategoryByIdAdmin(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
