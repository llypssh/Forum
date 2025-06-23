package com.forum.dao;

import com.forum.util.ConnectDB;
import com.forum.service.Review;


import java.sql.*;
import java.util.Scanner;

public class Select {
    public void Classy(String p_name) {
        Connection connection = ConnectDB.con();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            while (true) {
                System.out.println("========欢迎来到精选帖页面==========");
                String sql = "select num,username,title from post where sn = 1";
                preparedStatement = connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
                System.out.println("序号" + "\t|" + "用户名" + "\t|" + "标题");
                while (resultSet.next()) {
                    int num = resultSet.getInt("num");
                    String username = resultSet.getString("username");
                    String title = resultSet.getString("title");
                    System.out.println(num + "\t|" + username + "\t|" + title);

                }
                System.out.println("****输入0可返回论坛主页，输入对应序号查看内容****");
                Scanner scanner = new Scanner(System.in);
                int x = 0;
                try {
                    x = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("输入错误,请重新输入");
                    Classy(p_name);
                    break;
                }
                if (x == 0) {
                    Forum forum = new Forum();
                    forum.ForumMain(p_name);
                    break;
                } else {
                    String sql2 = "select title,username,content from post where num = ? and sn = 1";
                    preparedStatement = connection.prepareStatement(sql2);
                    preparedStatement.setInt(1, x);
                    ResultSet resultSet2 = preparedStatement.executeQuery();

                    if (resultSet2.next()) {    //查询到结果后
                        resultSet2 = preparedStatement.executeQuery();
                        while (resultSet2.next()) {
                            String title = resultSet2.getString("title");
                            String content = resultSet2.getString("content");
                            String username = resultSet2.getString("username");
                            System.out.println("-----------" + title + "---------------");
                            System.out.println("用户名：" + username);
                            System.out.println("内容：" + content);
                        }

                        String sql3 = "select username,content from review where r_num = '" + x + "'";
                        ResultSet resultSet3 = preparedStatement.executeQuery(sql3);
                        if (resultSet3.next()) {   //查询到回复结果
                            System.out.println("*****以下是回复内容*****");
                            resultSet3 = preparedStatement.executeQuery(sql3);
                            while (resultSet3.next()) {
                                String content2 = resultSet3.getString("content");
                                String username2 = resultSet3.getString("username");
                                System.out.println(username2 + ":\t" + content2);
                            }
                        } else {       //未查询到回复结果
                            System.out.println("*****暂时没有人回复哦！！！*****");
                        }
                        System.out.println("===输入0返回主页，输入1回复帖子==");
                        int j = 0;
                        try {
                            j = scanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("输入错误,请重新输入");
                            break;
                        }
                        if (j == 0) {
                            Forum forum = new Forum();
                            forum.ForumMain(p_name);
                            break;
                        } else if (j == 1) {
                            //get方法获取当前帖子序号
                            //跳转到Review类进行修改
                            Review review = new Review();
                            review.reply(x, 0, p_name);
                        } else {
                            System.out.println("输入错误,请重新输入");
                            break;
                        }
                    } else { //输入不存在的序号
                        System.out.println("您查找的内容消失了，看看其他的帖子吧！！！");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectDB.close(resultSet, preparedStatement, connection);
        }
    }
}
