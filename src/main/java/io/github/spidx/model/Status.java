package io.github.spidx.model;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE,
    CANCELLED;

    @Override
    public String toString() {
        return name();
    }
}