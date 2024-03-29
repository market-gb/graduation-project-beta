package ru.market_gb.core.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.ObjectError;
import ru.market_gb.api.exceptions.AppError;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Ошибки основного сервиса")
public class CoreAppError extends AppError {
    @Schema(description = "Коды ошибок сервиса", example = "PRODUCT_NOT_FOUND")
    private final Enum<?> code;
    @Schema(description = "Сообщения ошибок сервиса", example = "Продукт не найден")
    private final String message;
    @Schema(description = "Список ошибок валидации", example = "Поле названия товара не должно быть пустым")
    private List<ObjectError> errors;

    @Schema(description = "Коды ошибок основного сервиса", example = "PRODUCT_NOT_FOUND")
    public enum ServiceErrors {
        PRODUCT_NOT_FOUND, CORE_SERVICE_IS_BROKEN, VALIDATION_ERRORS, INVALID_PARAMS,
        CART_NOT_FOUND, CART_SERVICE_IS_BROKEN
    }

    public CoreAppError(Enum<?> code, String message) {
        this.code = code;
        this.message = message;
    }
}
