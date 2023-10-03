package ru.practicum.main.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShotDto {
    @Email(message = "Field: email. Error: must be email format.")
    @NotBlank(message = "Field: email. Error: must not be blank.")
    @Size(min = 6, message = "validation email size too short")
    @Size(max = 254, message = "validation email size too long")
    private String email;
    @NotBlank(message = "Field: name. Error: must not be blank.")
    @Size(min = 2, message = "validation name size too short")
    @Size(max = 250, message = "validation name size too long")
    private String name;
}
