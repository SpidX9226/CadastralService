package io.github.spidx9226.repository;

import io.github.spidx9226.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CitizenRepository {
    private CitizenRepository() {}

    public static void run() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            createSchema(connection);


        } catch (SQLException ex) {

        }
    }

    private static void addCitizen(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO citizens (full_name, passport_number, email)
                VALUES (?, ?, ?)
                """;

        var full_name = IO.readln("Введите полное имя: ");
        var passport_number = IO.readln("Введите номер пасспорта: ");
        var email = IO.readln("Введите email: ");

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, full_name);
            statement.setString(2, passport_number);
            statement.setString(2, email);
            statement.executeUpdate();
        }

        IO.println("Гражданин добавлен: " + full_name);
    }
    private static void createSchema(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS citizens (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    full_name VARCHAR(200) NOT NULL,
                    passport_number VARCHAR(6) NOT NULL UNIQUE,
                    email VARCHAR(30) UNIQUE
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}
