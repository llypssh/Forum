package com.forum.dao;


import com.forum.util.ConnectDB;
import com.forum.service.Review;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Tree {
    public void Secret(String p_name) {
        ConnectDB connectionDB = new ConnectDB();
        Connection connection = connectionDB.con();


        try {

            while (true) {
                //主页显示
                System.out.println("===这里是树洞页面，此页面发帖功能会隐藏用户名===");
                System.out.println("序号" + "\t|" + "标题");
                String sql = "select num,title from secret";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement1.executeQuery();
                while (resultSet.next()) {  //查询结果
                    int num = resultSet.getInt("num");
                    String title = resultSet.getString("title");
                    System.out.println(num + "\t|" + title);
                }
                System.out.println("输入0返回论坛主页，输入f发帖，输入序号查看具体内容");
                Scanner scanner = new Scanner(System.in);
                String x = scanner.next();
                if (x.equals("f")) {
                    //发帖功能
                    while (true) {
                        System.out.println("===欢迎使用树洞页面，现在可以使用发帖功能,请依次输入标题和内容===");
                        System.out.println("请输入标题");
                        String title1 = scanner.next();

                        System.out.println("请输入内容");
                        String content1 = scanner.next();

                        String sql1 = "insert into secret(title,content) values ('" + title1 + "','" + content1 + "')";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                        preparedStatement.executeUpdate(sql1);
                        System.out.println("提交成功");
                        System.out.println("===输入0返回树洞,输入1继续发帖===");
                        int a = scanner.nextInt();
                        if (a == 0) {
                            break;
                        }
                        if (a == 1) {
                            continue;
                        }

                    }
                } else if (x.equals("0")) {
                    System.out.println("跳转到论坛主页");
                    Forum forum = new Forum();
                    forum.ForumMain(p_name);
                    break;
                } else {    //查看详细页面
                    int n;
                    //将 String 型数据类型转换为 int 型
                    tag:
                    while (true) {
                        try {
                            n = Integer.parseInt(x);
                        } catch (Exception e) {
                            System.out.println("输入错误,请重新输入");
                            break;
                        }

                        //查找数据库的标题和内容
                        sql = "select title,content from secret where num = '" + n + "'";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        ResultSet rs = preparedStatement.executeQuery();


                        if (!rs.next()) {
                            //当输入不存在的序号
                            System.out.println("您查找的内容消失了，看看其他的帖子吧！！！");
                            break tag;
                        } else {
                            ResultSet rs1 = preparedStatement.executeQuery();
                            while (rs1.next()) {
                                String title = rs1.getString("title");
                                String content = rs1.getString("content");
                                System.out.println("----" + title + "-----");
                                System.out.println("内容：" + "\t" + content);
                            }
                            //查找对应内容回复
                            String sql3 = "select username,content from review where t_num = '" + x + "'";
                            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
                            ResultSet resultSet3 = preparedStatement3.executeQuery(sql3);
                            if (resultSet3.next()) {
                                System.out.println("*****以下是回复内容*****");
                                resultSet3 = preparedStatement3.executeQuery(sql3);
                                while (resultSet3.next()) {
                                    String content2 = resultSet3.getString("content");
                                    System.out.println("匿名用户" + ":\t" + content2);
                                }
                            } else {
                                System.out.println("*****暂时没有人回复哦！！！*****");
                            }
                            System.out.println("===输入0返回树洞,输入1回复帖子===");
                            int j = 0;
                            try {
                                j = scanner.nextInt();
                            } catch (Exception e) {
                                System.out.println("输入错误,请重新输入");
                                break;
                            }
                            if (j == 0) {
                                break;
                            } else if (j == 1) {
                                Review review = new Review();
                                review.reply(n, 1, p_name);
                            } else {
                                System.out.println("输入错误，请输入正确的选项");
                                break;
                            }
                        }
                    }
                }


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
