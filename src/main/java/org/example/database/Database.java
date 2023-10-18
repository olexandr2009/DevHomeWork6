package org.example.database;

import org.example.prefs.ConfigsNames;
import org.example.prefs.Configurations;

import java.sql.*;


public class Database {
    private static Database DEFAULT_DB;
    private Connection CONNECTION;

    public Database() {
        this(Configurations.Configs.getConfigAsString(ConfigsNames.DB_URL));
    }

    public Database(String dbURL) {
        try {
            CONNECTION = DriverManager.getConnection(
                    dbURL,
                    Configurations.Configs.getConfigAsString(ConfigsNames.USER_NAME_CONFIG_NAME),
                    Configurations.Configs.getConfigAsString(ConfigsNames.PASSWORD_CONFIG_NAME)
            );
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static Database getDefaultDB() {
        if (DEFAULT_DB == null) {
            DEFAULT_DB = new Database();
        }
        return DEFAULT_DB;
    }

    public Connection getConnection() {
        return CONNECTION;
    }

    public int executeUpdate(String sql) {
        try (Statement st = getStatement()) {
            return st.executeUpdate(sql);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return -1;
    }
    public PreparedStatement getPreparedStatement(String sql) {
        try {
            return CONNECTION.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() {
        try {
            return CONNECTION.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            CONNECTION.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

