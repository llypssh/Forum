package com.forum.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConnectDB {
    public static Connection con() {
        Properties properties = new Properties();
        InputStream resourceAsStream = ConnectDB.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Connection connection = null;
        try {
            properties.load(resourceAsStream);

            String driverClass = properties.getProperty("driverClass");
            String url = properties.getProperty("url");
            String name = properties.getProperty("name");
            String password = properties.getProperty("password");
            Class.forName(driverClass);

            connection = DriverManager.getConnection(url, name, password);

        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }

        return connection;
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();  // 归还连接
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
