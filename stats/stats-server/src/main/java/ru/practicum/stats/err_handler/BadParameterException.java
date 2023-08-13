package ru.practicum.stats.err_handler;

public class BadParameterException extends RuntimeException {

    public BadParameterException(String message) {
        super(message);
    }
}
