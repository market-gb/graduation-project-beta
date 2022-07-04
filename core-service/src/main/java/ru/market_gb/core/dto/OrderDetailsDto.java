package ru.market_gb.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Модель деталей заказа")
public class OrderDetailsDto {
    @Schema(description = "Адрес заказа", required = true, example = "603000, г. Москва, ул. Прямая, д.10, кв.1")
    @NotBlank(message = "Поле адреса не должно быть пустым")
    private String address;
    @Schema(description = "Телефон получателя", required = true, example = "222-22-22")
    @NotBlank(message = "Поле телефона пользователя не должно быть пустым")
    @Size(min = 5, message = "Длина номера телефона должна быть не менее 5 символов")
    private String phone;
}
