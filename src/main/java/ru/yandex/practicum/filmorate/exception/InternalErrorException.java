package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class InternalErrorException extends RuntimeException {

    private final HttpStatus status;

    public InternalErrorException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}