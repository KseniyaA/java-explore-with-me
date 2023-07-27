package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.valid.ValidateCategoryName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateCategoryName
public class CategoryDtoRequest {
    @NotBlank
    @Size(max = 50)
    private String name;
}
