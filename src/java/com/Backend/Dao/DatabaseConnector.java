package com.Backend.Dao;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides methods to connect to the database.
 */
public class DatabaseConnector {
    private static final String CONN_STRING = "YOUR_CONNECTION_STRING";


    private static final String USERNAME = "YOUR_USERNAME";
    private static final String PASSWORD = "YOUR_PASSWORD";

    private static final SQLServerDataSource dataSource = new SQLServerDataSource();
    private static Connection connection = null;

    static {
        dataSource.setURL(CONN_STRING);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
    }

    /**
     * Connects to the database and returns the connection.
     *
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
