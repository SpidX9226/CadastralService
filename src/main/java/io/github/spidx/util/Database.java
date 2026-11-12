package io.github.spidx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // ToDo: change to an actual data
    private static final String URL = "jdbc:postgresql://localhost:5432/cadastral_service";
    private static final String USER = "cadastral_service";
    private static final String PASSWORD = "1234password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}