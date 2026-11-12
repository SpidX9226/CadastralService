package io.github.spidx.exception;

public class RequestNotFoundException extends EntityNotFoundException {
    public RequestNotFoundException(String msg) {
        super(msg);
    }
}
