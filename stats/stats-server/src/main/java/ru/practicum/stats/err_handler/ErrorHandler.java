package ru.practicum.stats.err_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {BadParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadParameterException(final BadParameterException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse(
                "Ошибка входных параметров", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleArgumentValidException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        if (e.getBindingResult().getFieldErrors().isEmpty()) {
            final List<Violation> violations = e.getBindingResult().getAllErrors().stream()
                    .map(error -> new Violation(error.getObjectName(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return new ValidationErrorResponse(violations);
        } else {
            final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                    .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return new ValidationErrorResponse(violations);
        }
    }
}
