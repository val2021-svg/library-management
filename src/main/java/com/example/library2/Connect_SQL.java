package com.example.library2;

import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class Connect_SQL {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String CONN = "jdbc:mysql://localhost:3306/library2";

    Connection conn = null;
    Statement stm = null;
    ResultSet rs = null;

    public static Connection ConnectDb() throws Exception{
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(CONN, USERNAME, PASSWORD);
            System.out.println("Connected");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}