package ru.practicum.main.service.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @Builder.Default
    private Set<Long> events = Collections.emptySet();
    @Builder.Default
    private Boolean pinned = false;
    @NotBlank(message = "Field: title. Error: must not be blank.")
    @Size(min = 1, max = 50)
    private String title;
}
