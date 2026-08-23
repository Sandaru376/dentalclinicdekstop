package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Single place that knows how to talk to MySQL.
 * Requires the MySQL Connector/J jar to be added to the project's Libraries
 * (Right click project -> Properties -> Libraries -> Add JAR/Folder ->
 *  mysql-connector-j-8.x.x.jar). Download it from
 *  https://dev.mysql.com/downloads/connector/j/ or via Maven if you convert
 *  this to a Maven project later.
 */
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
