package ru.market_gb.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель категории продукта")
public class CategoryDto {
    @Schema(description = "ID категории", required = true, example = "1")
    private Long id;
    @NotBlank(message = "Поле названия категории не должно быть пустым")
    @Size(min = 5, message = "Название категории должно быть не короче 5 символов")
    @Schema(description = "Название категории", required = true, example = "Category#1")
    private String title;

    @Override
    public String toString() {
        return title;
    }
}
