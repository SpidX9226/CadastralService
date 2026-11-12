package io.github.spidx.proxy;

import io.github.spidx.model.Citizen;
import io.github.spidx.model.Model;
import io.github.spidx.util.Database;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CitizenProxy implements IProxy {

    @Override
    public <T extends Model> boolean Add(T model) {
        if (!(model instanceof Citizen citizen)) {
            return false;
        }

        var cmd =   """
                    INSERT INTO citizens
                    (id, full_name, date_of_birth, passport_series, passport_number)
                    VALUES
                    (?, ?, ?, ?, ?)
                    """;

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, model.GetId());
            statement.setString(2, citizen.GetFullName());
            statement.setDate(3, Date.valueOf(citizen.getDateOfBirth()));
            statement.setInt(4, citizen.GetPassportSeries());
            statement.setInt(5, citizen.GetPassportNumber());

            return statement.execute();
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Override
    public List<Citizen> GetAll() {
        var cmd = "SELECT * FROM citizens ORDER BY date_of_birth";

        List<Citizen> citizens = new ArrayList<>();

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                citizens.add(Citizen.TryParse(resultSet));
            }
            return citizens;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Nullable
    @Override
    public Citizen GetById(UUID id) {
        var cmd = "SELECT * FROM citizens WHERE id = ?";

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, id);
            try (var resSet = statement.executeQuery()) {
                return resSet.next() ? Citizen.TryParse(resSet) : null;
            }
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Override
    public <T extends Model> boolean Update(T model) {
        if (!(model instanceof Citizen citizen)) {
            return false;
        }

        var cmd = "UPDATE citizen SET full_name = ?, date_of_birth = ?, passport_series = ?, passport_number = ?";

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setString(1, citizen.GetFullName());
            statement.setDate(2, Date.valueOf(citizen.getDateOfBirth()));
            statement.setInt(3, citizen.GetPassportSeries());
            statement.setInt(4, citizen.GetPassportNumber());

            return statement.executeUpdate() > 0;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }

    @Override
    public boolean Delete(UUID id) {
        var cmd = "DELETE FROM citizens WHERE id = ?";

        try (var connection = Database.getConnection(); var statement = connection.prepareStatement(cmd)) {
            statement.setObject(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException err) {
            throw this.DatabaseError(err);
        }
    }


}
