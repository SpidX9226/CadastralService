package io.github.spidx.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Objects;

public class Citizen extends Model implements IUpdateable<Citizen> {
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

    @Override
    public Citizen Update(Citizen otherCitizen) {
        if (otherCitizen == null) {
            return this;
        }

        this.SetId(otherCitizen.GetId() != null ? otherCitizen.GetId() : this.GetId());
        this.SetFullName(Objects.requireNonNullElse(otherCitizen.GetFullName(), this.GetFullName()));
        this.SetDateOfBirth(Objects.requireNonNullElse(otherCitizen.getDateOfBirth(), this.getDateOfBirth()));

        if (otherCitizen.GetPassportSeries() != 0) {
            this.SetPassportSeries(otherCitizen.GetPassportSeries());
        }
        if (otherCitizen.GetPassportNumber() != 0) {
            this.SetPassportNumber(otherCitizen.GetPassportNumber());
        }

        return this;
    }

    public static Citizen TryParse(ResultSet resultSet) throws SQLException {
        return new Citizen(
                (UUID) resultSet.getObject("id"),
                resultSet.getString("full_name"),
                resultSet.getDate("date_of_birth").toLocalDate(),
                resultSet.getInt("passport_series"),
                resultSet.getInt("passport_series"));
    }
}
