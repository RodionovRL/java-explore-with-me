package ru.practicum.main.service.category.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.category.service.CategoryService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryPubController {
    private final CategoryService categoryService;


    @GetMapping("")
    public ResponseEntity<Collection<CategoryDto>> getAllCategoriesPub(
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Positive int size
    ) {

        log.info("receive GET request for return categories from={}, size={}", from, size);
        Collection<CategoryDto> categoriesDto = categoryService.getAllCategoriesPub(from, size);
        return new ResponseEntity<>(categoriesDto, HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryByIdPub(
            @PathVariable("catId") @NotEmpty Long categoryId
    ) {
        log.info("receive GET request for return category by categoryId={}", categoryId);
        CategoryDto categoryDto = categoryService.getCategoryByIdPub(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
}
