package com.skill_mentor.root.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    private final String url = "jdbc:mysql://localhost:3306/skill_mentor";
    private final String username = "root";
    private final String password = "Watapuluwa100A";

    public Connection getConnection() {
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(ClassNotFoundException exception){
            exception.printStackTrace();
        }
        catch(SQLException exception){
            throw new RuntimeException(exception);
        }
        return connection;
    }
}
