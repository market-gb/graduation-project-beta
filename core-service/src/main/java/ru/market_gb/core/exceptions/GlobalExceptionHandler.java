package ru.market_gb.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CoreServiceAppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new CoreServiceAppError(CoreServiceAppError.CoreServiceErrors.PRODUCT_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CoreServiceAppError> catchInvalidParamsException(InvalidParamsException e) {
        return new ResponseEntity<>(new CoreServiceAppError(CoreServiceAppError.CoreServiceErrors.INVALID_PARAMS, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CoreServiceAppError> catchValidationException(CoreValidationException e) {
        return new ResponseEntity<>(new CoreServiceAppError(CoreServiceAppError.CoreServiceErrors.VALIDATION_ERRORS, e.getMessage(), e.getErrors()), HttpStatus.BAD_REQUEST);
    }
}
