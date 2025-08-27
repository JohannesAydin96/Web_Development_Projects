package webshop.DAL.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database operations
 */
public class DatabaseUtil {
    // MySQL connection parameters
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/webshop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; // Using root with no password as discussed
    private static final String PASSWORD = ""; // Empty password for root user

    /**
     * Static initializer to load the JDBC driver
     */
    static {
        try {
            // Load the MySQL JDBC driver
            Class.forName(DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("ERROR: MySQL JDBC Driver not found! Make sure the driver JAR is in your classpath.");
        }
    }

    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Connect to MySQL using the provided credentials
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to connect to database!");
            throw e;
        }
    }

    /**
     * Close the database connection
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("ERROR: Failed to close database connection!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Close the statement
     * @param statement Statement to close
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("ERROR: Failed to close statement!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Close the result set
     * @param resultSet ResultSet to close
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("ERROR: Failed to close result set!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Close all database resources
     * @param connection Connection to close
     * @param statement Statement to close
     * @param resultSet ResultSet to close
     */
    public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }

    /**
     * Test the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}