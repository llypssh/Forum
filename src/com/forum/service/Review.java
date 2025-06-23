package com.forum.service;

import com.forum.util.ConnectDB;
import com.forum.dao.Forum;

import java.sql.*;
import java.util.Scanner;

public class Review {
    public void reply(int a,int b,String p_name) {
        ConnectDB connectionDB = new ConnectDB();
        Connection connection = connectionDB.con();
        Statement statement = null;
        int row ;
        while(true) {
            try {
                statement = connection.createStatement();
                System.out.println("===欢迎来到回复页面，请输入回复内容===");
                Scanner scanner = new Scanner(System.in);
                    String content1 = scanner.next();
                    //回复普通帖子（r_num = 1）
                    String sql1 = "insert into review(content,username,r_num) values ('" + content1 + "' , '" + p_name + "' , '" + a + "')";
                    //回复秘密树洞（t_num = 1）
                    String sql2 = "insert into review(content,username,t_num) values ('" + content1 + "' , '" + p_name + "' , '" + a + "')";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
                    if (b==0) {  //判断是普通帖子还是树洞
                        //b == 0 普通帖子
                        row = preparedStatement.executeUpdate(sql1);
                    }else {
                        //b != 0 秘密树洞
                        row = preparedStatement1.executeUpdate(sql2);
                    }

                    if (row == 1) {
                        System.out.println("成功发表评论");
                        break;
                    } else {
                        System.out.println("发表失败,请重新输入");
                        reply(a,b,p_name);
                        break;
                    }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
