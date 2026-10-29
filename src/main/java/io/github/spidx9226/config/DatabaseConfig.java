package io.github.spidx9226.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/cadastral_service";;
    private static final String USER = "postgres";
    private static final String PASSWORD =  System.getenv("DB_PASSWORD");

    private DatabaseConfig() {}

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable DB_PASSWORD is must be set"
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
