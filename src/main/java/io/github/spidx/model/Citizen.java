package io.github.spidx.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class Citizen extends Model {
    private String _fullName;
    private LocalDate _dateOfBirth;
    private int _passportSeries;
    private int _passportNumber;

    public Citizen(UUID id, String fullName, LocalDate dateOfBirth, int passportSeries, int passportNumber) {
        super(id);
        _fullName = fullName;
        _dateOfBirth = dateOfBirth;
        _passportSeries = passportSeries;
        _passportNumber = passportNumber;
    }

    public String GetFullName() {
        return _fullName;
    }
    public void SetFullName(String fullName) {
        _fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return _dateOfBirth;
    }
    public void SetDateOfBirth(LocalDate dateOfBirth) {
        _dateOfBirth = dateOfBirth;
    }

    public int GetPassportSeries() {
        return _passportSeries;
    }
    public void SetPassportSeries(int passportSeries) {
        _passportSeries = passportSeries;
    }

    public int GetPassportNumber() {
        return _passportNumber;
    }
    public void SetPassportNumber(int passportNumber) {
        _passportNumber = passportNumber;
    }

    public static Citizen TryParse(ResultSet resultSet) throws SQLException {
        return new Citizen(
                (UUID) resultSet.getObject("id"),
                resultSet.getString("full_name"),
                resultSet.getDate("date_of_birth").toLocalDate(),
                resultSet.getInt("passport_series"),
                resultSet.getInt("passport_series")
        );
    }
}