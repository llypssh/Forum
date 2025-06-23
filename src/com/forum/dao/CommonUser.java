package com.forum.dao;

import com.forum.service.Review;
import com.forum.util.ConnectDB;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class CommonUser {
    static Scanner scanner = new Scanner(System.in);

    public static void CheckPosts(String username) throws IOException {//查看帖子
        Forum forum = new Forum();
        forum.ForumMain(username);
    }

    public static void MyPosts(String username1) throws SQLException {   //查看自己的帖子
        //利用hashmap存放查询到的个人帖子键值对
        HashMap<Integer, String> map = new HashMap<>();
        Connection connection = ConnectDB.con();    //数据库连接
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //查看自己的普通帖子
        String sql_c = "select title from post where username = ?";
        //查看自己的秘密树洞帖子
        String sql_t = "select title from secret where username = ?";
        //查询普通帖子
        preparedStatement = connection.prepareStatement(sql_c);
        preparedStatement.setString(1, username1);
        resultSet = preparedStatement.executeQuery();
        //查询秘密树洞帖子
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql_t);
        preparedStatement1.setString(1, username1);
        ResultSet resultSet1 = preparedStatement1.executeQuery();

        int i = 0;  //标记帖子序号
        System.out.println("以下是" + username1 + "的帖子：");
        //论坛主页帖子
        while (resultSet.next()) {
            String title = resultSet.getString("title");
            i++;
            System.out.println(i + "." + "\t" + title + "（论坛主页）");
            map.put(i, title);
        }
        //区分普通帖子和秘密树洞
        int temp = i;
        //秘密树洞帖子
        while (resultSet1.next()) {
            String title2 = resultSet1.getString("title");
            i++;
            System.out.println(i + "." + "\t" + title2 + "（秘密树洞）");
            map.put(i, title2);
        }
        //操作
        System.out.println("输入0返回用户主页，输入d删除帖子，输入对应序号查看帖子");
        String s = scanner.next();

        while (true) {
            if (s.equals("0")) { //退出
                break;
            } else if (s.equals("d") || s.equals("D")) {
                //利用map的键值对删除
                int x = 0;
                while (true) {
                    System.out.println("选择需要删除的帖子序号");
                    //防止非法输入
                    try {
                        x = scanner.nextInt();
                        break;
                    } catch (Exception e) {
                        System.out.println("请输入正确的值");
                        break;
                    }
                }
                //获得删除的标题
                String delPost = map.get(x);
                String sqlDel;
                if (temp >= x) { //temp >= x为普通帖子
                    sqlDel = "DELETE FROM post WHERE username = ? and title = ?";
                } else if (temp < i) { //temp < x 为秘密树洞帖子
                    sqlDel = "DELETE FROM secret WHERE username = ? and title = ?";
                } else {
                    System.out.println("输入的序号不存在，请重新输入");
                    continue;
                }
                preparedStatement = connection.prepareStatement(sqlDel);
                preparedStatement.setString(1, username1);
                preparedStatement.setString(2, delPost);
                int rsDel = preparedStatement.executeUpdate();
                if (rsDel != 0) {
                    System.out.println("删除成功");
                    break;
                } else {
                    System.out.println("删除失败");
                    break;
                }
            } else {    //查看对应帖子（查询数据库中的标题和用户名）
                int n;
                try {  //防止输入有误
                    n = Integer.parseInt(s);
                } catch (Exception e) {
                    System.out.println("输入错误,请重新输入");
                    MyPosts(username1);
                    break;
                }
                //主题内容
                String sql1;
                //回复内容
                String sql2;
                //
                String title = map.get(n);
                if (temp >= n) {    //普通帖子
                    sql1 = "select username,title,content,num from post where username = ? and title = ? ";
                    sql2 = "select username,content from review where r_num = ? ";
                } else {        //秘密树洞
                    sql1 = "select username,title,content,num from secret where username = ? and title = ?";
                    sql2 = "select username,content from review where t_num = ?";
                }
                //处理主题内容
                preparedStatement = connection.prepareStatement(sql1);
                preparedStatement.setString(1, username1);
                preparedStatement.setString(2, title);
                ResultSet rs = preparedStatement.executeQuery();
                //获取当前帖子序号
                int n_num = 0;
                if (!rs.next()) {
                    System.out.println("您查找的内容消失了!!!");
                    break;
                } else {
                    rs = preparedStatement.executeQuery();
                    //显示详细内容
                    while (rs.next()) {
                        String name = rs.getString("username");
                        String title_c = rs.getString("title");
                        n_num = rs.getInt("num");
                        String content = rs.getString("content");
                        System.out.println("--------" + title_c + "----------");
                        if (temp >= n) {   //树洞内容不可查看用户名
                            System.out.println("用户名：" + "\t" + name);
                        }
                        System.out.println("内容:" + "\t" + content);
                    }
                    //显示回复内容
                    preparedStatement = connection.prepareStatement(sql2);
                    preparedStatement.setInt(1,n_num);
                    ResultSet rs2 = preparedStatement.executeQuery();
                    if (rs2.next()) {
                        //显示帖子回复内容
                        System.out.println("******以下是回复内容******");
                        rs2 = preparedStatement.executeQuery();
                        while (rs2.next()) {
                            String content2 = rs2.getString("content");
                            String username2 = rs2.getString("username");
                            //区分树洞和普通帖子
                            if (temp >= n) {
                                System.out.println(username2 + ":\t" + content2);
                            } else {
                                System.out.println("匿名用户：" + content2);
                            }
                        }
                    } else { //未查询到结果
                        System.out.println("*****暂时没有人回复哦！！！*****");
                    }

                    System.out.println("===输入0返回，输入1回复帖子==");
                    int j = 0;
                    try {
                        j = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("输入错误,请重新输入");
                        break;
                    }
                    if (j == 0) {
                        MyPosts(username1);
                        break;
                    } else if (j == 1) {
                        //get方法获取当前帖子序号
                        //跳转到Review类进行修改
                        Review review = new Review();
                        review.reply(n, 0, username1);
                    } else {
                        System.out.println("输入错误,请重新输入");
                        break;
                    }
                }

            }


        }
    }


    public static String AlterPassWord(String username, String oldPassword) throws SQLException {//修改密码
        Connection connection = ConnectDB.con();
        String newPassword1 = "";
        String newPassword2 = "";
        PreparedStatement preparedStatement = null;
        while (true) {
            System.out.println("请输入正在使用的密码:");
            String s = scanner.next();
            //验证是否本人修改密码
            if (!(s.equals(oldPassword))) {
                System.out.println("密码输入错误");
                continue;
            }
            //执行修改密码
            System.out.print("请输入新密码（长度为6-16位）: ");
            newPassword1 = scanner.next();
            System.out.print("请再次输入密码: ");
            newPassword2 = scanner.next();

            if (!newPassword1.equals(newPassword2)) {
                System.out.println("两次密码不一致，请重新输入！！！");
                continue;
            } else if (newPassword1.length() < 6 || newPassword1.length() > 16) {
                System.out.println("密码过长或过短");
                continue;
            } else if (newPassword1.equals(oldPassword)) {
                System.out.println("与旧密码重复");
                continue;
            }

            // 密码修改sql
            String sql = "update forumuser set password=? where password=? and username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newPassword1);
            preparedStatement.setString(2, oldPassword);
            preparedStatement.setString(3, username);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("密码修改成功！");
                break;
            }
            ConnectDB.close(null, preparedStatement, connection);
        }
        return newPassword1;
    }

    //个人用户主页
    public static void Common(String username) throws SQLException {
        System.out.println();

        System.out.print("\t**********您好，这里是个人中心，您想做什么？***********\n" +
                "        \t------\t当前用户:" + username + "\t------    \t\n" +
                "\t*          1. 查看帖子                         \t*\n" +
                "\t*          2. 修改密码                         \t*\n" +
                "\t*          3. 查看和删除自己的帖子               \t*\n" +
                "\t*          0. 退出                            \t*\n" +
                "\t*************************************************\n");
        System.out.print("请选择操作：");
    }


}
