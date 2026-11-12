package io.github.spidx.proxy;

import io.github.spidx.model.Model;
import io.github.spidx.model.Request;
import io.github.spidx.util.Database;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestProxy implements IProxy {
    @Override
    public <T extends Model> boolean Add(T model) {
        if (!(model instanceof Request request)) {
            return false;
        }

        var cmd =   """
                    INSERT INTO requests
                    (id, citizen_id, status, open_at, closed_at)
                    VALUES
                    (?, ?, ?, ?, ?)
                    """;

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, request.GetId());
            statement.setObject(2, request.GetCitizenId());
            statement.setString(3, request.GetStatus().toString());
            statement.setObject(4, request.GetOpenAt());
            statement.setObject(5, request.GetClosedAt());

            return statement.executeUpdate() > 0;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Override
    public List<Request> GetAll() {
        var cmd = "SELECT * FROM requests ORDER BY open_at";

        List<Request> requests = new ArrayList<>();

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(Request.TryParse(resultSet));
            }
            return requests;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Nullable
    @Override
    public Request GetById(UUID id) {
        var cmd = "SELECT * FROM requests WHERE id = ?";

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, id);
            try (var resSet = statement.executeQuery()) {
                return resSet.next()
                        ? Request.TryParse(resSet)
                        : null;
            }
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Override
    public <T extends Model> boolean Update(T model) {
        if (!(model instanceof Request request)) {
            return false;
        }

        var cmd =   """
                    UPDATE requests
                    SET citizen_id = ?, status = ?, open_at = ?, closed_at = ?
                    WHERE id = ?
                    """;

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, request.GetCitizenId());
            statement.setString(2, request.GetStatus().toString());
            statement.setObject(3, request.GetOpenAt());
            statement.setObject(4, request.GetClosedAt());
            statement.setObject(5, request.GetId());

            return statement.executeUpdate() > 0;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Override
    public boolean Delete(UUID id) {
        var cmd = "DELETE FROM requests WHERE id = ?";

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }
}