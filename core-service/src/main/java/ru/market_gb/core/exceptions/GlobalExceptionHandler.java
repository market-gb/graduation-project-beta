package ru.market_gb.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ServiceAppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ServiceAppError(ServiceAppError.ServiceErrors.PRODUCT_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ServiceAppError> catchInvalidParamsException(InvalidParamsException e) {
        return new ResponseEntity<>(new ServiceAppError(ServiceAppError.ServiceErrors.INVALID_PARAMS, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ServiceAppError> catchValidationException(CoreValidationException e) {
        return new ResponseEntity<>(new ServiceAppError(ServiceAppError.ServiceErrors.VALIDATION_ERRORS, e.getMessage(), e.getErrors()), HttpStatus.BAD_REQUEST);
    }
}
