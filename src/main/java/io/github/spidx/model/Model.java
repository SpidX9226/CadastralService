package io.github.spidx.model;

import java.util.UUID;

public abstract class Model {
    private UUID _id;

    public Model(UUID id) {
        _id = id;
    }

    public UUID GetId() {
        return _id;
    }

    public void SetId(UUID id) {
        _id = id;
    }
}
