package LogisticsManagementSystem;
//DatabaseConnection.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 private static final String URL = "jdbc:mysql://localhost:3306/logisticsmanagementsystem";
 private static final String USER = "root";
 private static final String PASSWORD = "Smit@22";
 
 public static Connection getConnection() throws SQLException {
     return DriverManager.getConnection(URL, USER, PASSWORD);
 }
}