package io.github.spidx.exception;

public class EntityNotFoundException extends RuntimeException{
    EntityNotFoundException(String msg) {
        super(msg);
    }
}