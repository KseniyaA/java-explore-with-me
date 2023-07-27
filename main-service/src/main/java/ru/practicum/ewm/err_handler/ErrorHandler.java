package ru.practicum.ewm.err_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.BadParameterException;
import ru.practicum.ewm.exception.ConflictOperationException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.UnavailableOperationException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final EntityNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse(
                "Ошибка получения по id", e.getMessage()
        );
    }

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIncorrectParameterException(final UnavailableOperationException e) {
        log.debug("Получен статус 403 Forbidden {}", e.getMessage(), e);
        return new ErrorResponse(
                "Операция недоступна", e.getMessage()
        );
    }

    @ExceptionHandler(ConflictOperationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictOperationException(final ConflictOperationException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return new ErrorResponse(
                "Операция недоступна", e.getMessage()
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

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentValidException(final MissingPathVariableException e) {
        log.debug("Получен статус 400 BadRequest {}", e.getMessage(), e);
        return new ErrorResponse(
                "Потерян обязательный параметр", e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentValidException(final Exception e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return new ErrorResponse(
                "Непредвиденная ошибка: ", e.getMessage()
        );
    }
}
