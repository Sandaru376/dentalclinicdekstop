package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/dental_clinic?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234"; // change to your MySQL Workbench root password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found on classpath. " +
                    "Add mysql-connector-j jar to the project Libraries.", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
