package ru.practicum.main.service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotEmpty(message = "Field: name. Error: must not be empty.")
    @Size(min = 1, message = "Field: name. Error: name length too short(<1).")
    @Size(max = 50, message = "Field: name. Error: name length too long(>50).")
    private String name;
}
