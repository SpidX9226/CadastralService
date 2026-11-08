package io.github.spidx.proxy;

import io.github.spidx.model.Model;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IProxy {
    <T extends Model> boolean Add(T model);
    <T extends Model> List<T> GetAll();
    Model GetById(UUID id);
    <T extends Model> boolean Update(T model);
    boolean Delete(UUID id);

    default RuntimeException DatabaseError(SQLException error) {
        return new IllegalStateException(
                "Не удалось выполнить операцию с базой данных. Проверьте подключение и введённые данные.",
                error
        );
    }
}