package com.forum.dao;

import com.forum.*;
import com.forum.controller.Client;
import com.forum.util.ConnectDB;
import com.forum.pojo.User;
import com.forum.service.Review;


import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class Forum {
    public static int n;

    public void ForumMain(String p_name) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8000);
        ConnectDB connectionDB = new ConnectDB();
        Connection connection = connectionDB.con();
        Statement statement = null;
        User user = new User();


        try {
            while (true) {
                statement = connection.createStatement();

                System.out.println("=================这里是论坛主页，看看大家发的帖子吧========================");
                //显示论坛主页的所有帖子
                String sql = "select num,username,title from post";

                ResultSet resultSet = statement.executeQuery(sql);

                System.out.println("序号" + "\t|" + "用户名" + "\t" + "\t|" + "标题");
                while (resultSet.next()) {
                    int number = resultSet.getInt("num");
                    String username = resultSet.getString("username");
                    String title = resultSet.getString("title");
                    System.out.println(number + "\t|" + username + "\t" + "\t|" + title);
                }
                System.out.println("******输入p进入个人中心，输入t进入秘密树洞版块，输入s进入精选帖子，输入f发帖，输入帖子序号查询帖子详细内容******");
                Scanner scanner = new Scanner(System.in);
                String x = scanner.next();
                if (x.equals("p") || x.equals("P")) {    //个人页面
                    if (!p_name.equals("admin")) {  //非管理员用户
                        Client.personPage(socket);
                    } else {    //管理员用户
                        Client.adminPage(socket);
                    }
                    break;
                } else if (x.equals("t") || x.equals("T")) { //进入树洞页面
                    Tree tree = new Tree();
                    tree.Secret(p_name);
                    break;
                } else if (x.equals("s") || x.equals("S")) {  //进入精选帖子页面
                    Select select = new Select();
                    select.Classy(p_name);
                    break;
                } else if (x.equals("f") || x.equals("F")) {    //发帖
                    while (true) {
                        System.out.println("=========现在可以使用发帖功能,请依次输入标题和内容==========");
                        System.out.println("请输入标题");
                        String title1 = scanner.next();

                        System.out.println("请输入内容");
                        String content1 = scanner.next();

                        String sql1 = "insert into post(username,title,content) values ('" + p_name + "','" + title1 + "','" + content1 + "')";

                        PreparedStatement preparedStatement = connection.prepareStatement(sql1);

                        int row = preparedStatement.executeUpdate(sql1);
                        System.out.println("发表成功");
                        System.out.println("===输入0返回主页,输入1继续发帖===");
                        int a = 0;
                        try {
                            a = scanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("输入错误,返回主页");
                            break;
                        }
                        if (a == 0) {
                            break;
                        }
                        if (a == 1) {
                            continue;
                        }

                    }
                } else {

                    //查看详细内容部分
                    tag:
                    while (true) {
                        try {  //防止输入有误
                            n = Integer.parseInt(x);
                        } catch (Exception e) {
                            System.out.println("输入错误,请重新输入");
                            break;
                        }

                        String sql1 = "select username,title,content from post where num = '" + n + "' ";
                        String sql2 = "select username,content from review where r_num = '" + n + "' ";


                        while (true) {
                            ResultSet resultSet1 = statement.executeQuery(sql1);
                            //显示帖子具体内容 (未完成输入序号不存在情况)

                            if (resultSet1.next()) {
                                String content1 = resultSet1.getString("content");
                                String username1 = resultSet1.getString("username");
                                String title1 = resultSet1.getString("title");
                                System.out.println("--------" + title1 + "----------");
                                System.out.println("用户名：" + "\t" + username1);
                                System.out.println("内容:" + "\t" + content1);

                            } else {
                                System.out.println("您查找的内容消失了，看看其他的帖子吧!!!");
                                break tag;
                            }

                            //查询回复内容
                            ResultSet resultSet2 = statement.executeQuery(sql2);
                            if (resultSet2.next()) {
                                //显示帖子回复内容
                                System.out.println("******以下是回复内容******");
                                resultSet2 = statement.executeQuery(sql2);
                                while (resultSet2.next()) {
                                    String content2 = resultSet2.getString("content");
                                    String username2 = resultSet2.getString("username");
                                    System.out.println(username2 + ":\t" + content2);
                                }
                            } else { //未查询到结果
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
                                ForumMain(p_name);
                                break;
                            } else if (j == 1) {
                                //get方法获取当前帖子序号
                                //跳转到Review类进行修改
                                Review review = new Review();
                                review.reply(n, 0, p_name);
                            } else {
                                System.out.println("输入错误,请重新输入");
                                break;
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPostNum() {
        return this.n;
    }

}
