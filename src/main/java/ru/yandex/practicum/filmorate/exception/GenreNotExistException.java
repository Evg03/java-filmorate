package ru.yandex.practicum.filmorate.exception;

public class GenreNotExistException extends RuntimeException {
    public GenreNotExistException(String message) {
        super(message);
    }
}
