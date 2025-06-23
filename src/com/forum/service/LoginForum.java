package com.forum.service;

import com.forum.util.ConnectDB;
import com.forum.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginForum {


    static Scanner sc = new Scanner(System.in);
    static ConnectDB connectDB = new ConnectDB();
    static Connection connection = connectDB.con();
    static String username;
    static String password;
    static Map<String, String> loginInfo = new HashMap<>();


    public static void land() {
        System.out.println("*******欢迎登陆校园论坛*******");
        System.out.println("请输入数字 1.进行注册 2.页面登录 0.退出");
    }


    public static User register() {
        User user1 = new User();
        String register_username;
        String register_password;
        while (true) {
            System.out.println("请输入需要注册的用户名（4-14位）:");
            register_username = sc.next();

            System.out.println("请输入密码（6-16位）:");
            register_password = sc.next();


            user1.setUsername(register_username);
            user1.setPassword(register_password);
            return user1;
        }
    }

    //服务器添加注册结果
    public static int add_register(String register_username, String register_password) throws SQLException {
        int flag = 0;
        try {
            if (register_username.length() < 3 || register_username.length() > 13) {
                flag = 2;
            } else if (register_password.length() < 5 || register_password.length() > 15) {
                flag = 3;
            } else {
                String sql2 = "insert into forumuser(username,password) values(?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
                preparedStatement1.setString(1, register_username);
                preparedStatement1.setString(2, register_password);
                preparedStatement1.executeUpdate();
                flag = 4;
            }
        } catch (SQLException e) {
            flag = 1;
        }

        return flag;
    }

    public static User login() throws SQLException {    //登录界面
        User user = new User();
        System.out.println("请输入用户名：");
        username = sc.next();
        System.out.println("请输入密码：");
        password = sc.next();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    // 验证登录是否正确
    public static int isLogin(String username,String password) throws SQLException {
        boolean f = false;
        //结果标识  0：未查询到结果 ，1：查询为管理员用户，2：查询为一般用户
        int count = 0;
        //查询数据库
        String sql1 = "select * from forumuser where username = ? and password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql1);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = null;
        resultSet = preparedStatement.executeQuery();

        //是否查询到结果
        if (resultSet.next()) {

            f = true;
        }
        if (f) {
            if (username.equals("admin")) {
                //管理员登录
                count = 1;
            } else {
                //普通用户登录
                count = 2;
            }
        }
        return count;
    }
}

