package io.github.spidx.model;

public interface IUpdateable<T> {
    T Update(T otherObject);
}
