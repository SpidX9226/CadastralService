package io.github.spidx.model;

import java.util.UUID;

public abstract class Model {
    private UUID _id;

    public Model(UUID id) {
        _id = id;
    }

    public UUID getId() {
        return _id;
    }

    public void setId(UUID id) {
        _id = id;
    }
}