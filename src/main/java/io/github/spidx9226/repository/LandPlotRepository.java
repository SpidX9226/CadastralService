package io.github.spidx9226.repository;

import io.github.spidx9226.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class LandPlotRepository {
    private LandPlotRepository() {}

    public static void run() {
        try (Connection connection = DatabaseConfig.getConnection()) {

        } catch (SQLException ex) {

        }
    }

    private static void createSchema(Connection connection) throws SQLException {
        String sql = """
                
                """;
    }
}