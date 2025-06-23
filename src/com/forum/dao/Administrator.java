package com.forum.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

import com.forum.util.ConnectDB;

public class Administrator {
    private static final Scanner scanner = new Scanner(System.in);
    static Connection connection = ConnectDB.con();

    //管理员界面主页
    public static void Page(String username) {

        System.out.println("\t*\t*****************管理员控制台**************\t*");
        System.out.print("\t*\t               请输入操作代码：               \t*\n" +
                         "\t*\t          001. 进入论坛主页                  \t*\n" +
                         "\t*\t          002. 进入秘密树洞                  \t*\n" +
                         "\t*\t          003. 查看精选帖子                  \t*\n" +
                         "\t*\t          004. 修改密码                     \t*\n" +
                         "\t*\t          005. 查看或删除自己的帖子           \t*\n" +
                         "\t*\t          006. 删除帖子                     \t*\n" +
                         "\t*\t          007. 加入精选                     \t*\n" +
                         "\t*\t          008. 退出                        \t*\n" +
                         "\t*************************************************\n"+
                         "请选择操作：");
    }


    public static void CheckMyPosts(String username1) throws SQLException {   //查看自己的帖子
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
                    break;
                }
                String sql1;
                String sql2;
                String title = map.get(n);
                if (temp >= n) {
                    sql1 = "select username,title,content from post where username = ? and title = ? ";
                    sql2 = "select username,content from review where username = ? and title = ? ";
                } else {
                    sql1 = "select user,title,content from secret where username = ? and title = ?";
                    sql2 = "select username,content from review where t_num = ";
                }
            }


        }
    }


    public static String AlterPassWord(String username, String oldPassword) throws SQLException {//修改密码
        Connection connection = ConnectDB.con();
        String newPassword1 = "";
        String newPassword2 = "";
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newPassword1);
            preparedStatement.setString(2, oldPassword);
            preparedStatement.setString(3, username);

            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                System.out.println("密码修改成功！");
                break;
            }
        }

        return newPassword1;
    }

    // 删除帖子
    public static void deletePost() {
        try {
            boolean flag;
            int num = 0;
            //查看普通帖子
            String sql1 = "select * from post";
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("=====普通帖子=====");
            //显示普通帖子
            while (resultSet.next()) {
                int number = resultSet.getInt("num");
                String name = resultSet.getString("username");
                String title = resultSet.getString("title");
                System.out.println(number + "\t|" + name + "\t|" + title);
            }
            //查看树洞帖子
            String sql2 = "select * from secret";
            preparedStatement = connection.prepareStatement(sql2);
            ResultSet resultSet2 = preparedStatement.executeQuery();
            System.out.println("=====秘密树洞=====");
            //显示所有树洞帖子
            while (resultSet2.next()) {
                int number = resultSet2.getInt("num");
                String title = resultSet2.getString("title");
                System.out.println(number + "\t|" + title);
            }
            //判断del板块内容
            while (true) {
                System.out.println("请选择删除帖子的板块,普通帖子为f,秘密树洞为s");
                String type = scanner.next();
                if (type.equals("f")) {
                    flag = true;
                    break;
                } else if (type.equals("s")) {
                    flag = false;
                    break;
                } else {
                    System.out.println("输入有误！！！");
                    continue;
                }
            }
            //输入删除的序号
            while(true) {

                System.out.println("请输入删除的帖子序号：");
                try {
                    num = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("输入错误，请重新输入");
                    break;
                }
                //删除普通帖子
                String sql3 = "DELETE FROM post WHERE num = ?";
                String sql4 = "DELETE FROM secret WHERE num = ?";
                PreparedStatement pstmt = null;

                if (flag) {
                    pstmt = connection.prepareStatement(sql3);
                } else {
                    pstmt = connection.prepareStatement(sql4);
                }
                pstmt.setInt(1, num);
                int row = pstmt.executeUpdate();
                if (row == 0) {
                    System.out.println("删除失败，未找到对应序号，请重新输入！");
                    continue;
                }else{
                    System.out.println("删除成功！");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("删除失败,请重试");
            deletePost();
        }
    }

    public static void selectPost() throws SQLException {
        boolean flag;
        int num = 0;
        //查看普通帖子
        String sql1 = "select * from post";
        PreparedStatement preparedStatement = connection.prepareStatement(sql1);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("=====普通帖子=====");
        while (resultSet.next()) {
            int number = resultSet.getInt("num");
            String name = resultSet.getString("username");
            String title = resultSet.getString("title");
            System.out.println(number + "\t|" + name + "\t|" + title);
        }
        System.out.println("请输入要精选帖子的序号");
        num = scanner.nextInt();

        // 精选帖子的SQL语句
        String sql = "UPDATE post SET sn = 1 WHERE num = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, num);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("精选失败，请重试");
            selectPost();
        }
    }
}



