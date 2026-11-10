package io.github.spidx.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String msg) {
        super(msg);
    }
}
