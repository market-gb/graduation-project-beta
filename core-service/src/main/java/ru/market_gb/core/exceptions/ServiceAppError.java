package ru.market_gb.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceAppError {
    private final Enum<?> code;
    private final String message;
    private List<ObjectError> errors;

    public enum ServiceErrors {
        PRODUCT_NOT_FOUND, CORE_SERVICE_IS_BROKEN, VALIDATION_ERRORS, INVALID_PARAMS,
        CART_NOT_FOUND, CART_SERVICE_IS_BROKEN
    }

    public ServiceAppError(Enum<?> code, String message) {
        this.code = code;
        this.message = message;
    }
}
