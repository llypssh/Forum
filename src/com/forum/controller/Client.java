package com.forum.controller;


import com.forum.*;
import com.forum.dao.*;
import com.forum.pojo.User;
import com.forum.service.LoginForum;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

import static java.lang.Thread.sleep;


public class Client {
    static User user1 = new User();
    static int count = 0;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8000);
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                //登录页面
                homepage(socket);
                //个人主页
                if (count == 1) {   //管理员页面
                    System.out.println(" __        __  _____   _        ____    ___    __  __   _____  \n" +
                            " \\ \\      / / | ____| | |      / ___|  / _ \\  |  \\/  | | ____| \n" +
                            "  \\ \\ /\\ / /  |  _|   | |     | |     | | | | | |\\/| | |  _|   \n" +
                            "   \\ V  V /   | |___  | |___  | |___  | |_| | | |  | | | |___  \n" +
                            "    \\_/\\_/___ |_____| |_____|__\\____|  \\___/__|_|  |_| |_____| \n" +
                            "       |_   _|  / _ \\      |_   _| | | | | | ____|             \n" +
                            "         | |   | | | |       | |   | |_| | |  _|               \n" +
                            "         | |   | |_| |       | |   |  _  | | |___              \n" +
                            "        _|_|_   \\___/     ___|_|   |_| |_| |_____|             \n" +
                            "       |  ___|   / _ \\   |  _ \\   | | | |  |  \\/  |            \n" +
                            "       | |_     | | | |  | |_) |  | | | |  | |\\/| |            \n" +
                            "       |  _|    | |_| |  |  _ <   | |_| |  | |  | |            \n" +
                            "       |_|       \\___/   |_| \\_\\   \\___/   |_|  |_|   ");
                //loading加载动画（美观）
                    System.out.print("\n Loading");
                    for (int i = 0; i < 5; i++) {
                        System.out.print(".");
                        sleep(700);
                    }
                    System.out.println("Complete！！！");
                    adminPage(socket);
                } else if (count == 2) {  //普通用户
                    System.out.println(" __        __  _____   _        ____    ___    __  __   _____  \n" +
                            " \\ \\      / / | ____| | |      / ___|  / _ \\  |  \\/  | | ____| \n" +
                            "  \\ \\ /\\ / /  |  _|   | |     | |     | | | | | |\\/| | |  _|   \n" +
                            "   \\ V  V /   | |___  | |___  | |___  | |_| | | |  | | | |___  \n" +
                            "    \\_/\\_/___ |_____| |_____|__\\____|  \\___/__|_|  |_| |_____| \n" +
                            "       |_   _|  / _ \\      |_   _| | | | | | ____|             \n" +
                            "         | |   | | | |       | |   | |_| | |  _|               \n" +
                            "         | |   | |_| |       | |   |  _  | | |___              \n" +
                            "        _|_|_   \\___/     ___|_|   |_| |_| |_____|             \n" +
                            "       |  ___|   / _ \\   |  _ \\   | | | |  |  \\/  |            \n" +
                            "       | |_     | | | |  | |_) |  | | | |  | |\\/| |            \n" +
                            "       |  _|    | |_| |  |  _ <   | |_| |  | |  | |            \n" +
                            "       |_|       \\___/   |_| \\_\\   \\___/   |_|  |_|   ");
                    //loading加载动画（美观）
                    System.out.print("\n Loading");
                    for (int i = 0; i < 5; i++) {
                        System.out.print(".");
                        sleep(600);
                    }
                    System.out.println("Complete！！！");
                    sleep(600);
                    personPage(socket);
                }
            }
            //退出时给客户端发送退出提示
            //outputStream.;


        } catch (Exception e) {
            System.out.println("服务器故障,请联系相关人员修复.......");

            System.exit(0);
        }


    }


    //登录主页
    private static void homepage(Socket socket) throws IOException, SQLException, ClassNotFoundException {

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


        String choice;
        tag:
        while (true) {

            LoginForum.land();
            choice = scanner.next();
            objectOutputStream.writeObject(choice);
            switch (choice) {
                case "1": //注册
                    User register = LoginForum.register();
                    objectOutputStream.writeObject(register);

                    Object s = objectInputStream.readObject();
                    if (s != null) {
                        System.out.println(s);
                    }
                    break;

                case "2":  //登录
                    User login = LoginForum.login();
                    objectOutputStream.writeObject(login);
                    //防止重复登录
                    boolean b = (boolean) objectInputStream.readObject();
                    if (b) {
                        System.out.println(login.getUsername() + "已在线，请勿重复登录");
                        continue;
                    }
                    count = (int) objectInputStream.readObject();
                    if (count != 0) {
                        user1 = login;
                        break tag;
                    } else {
                        System.out.println("账号或密码错误");
                    }
                    break;

                case "0":  //退出登录
                    System.out.println("期待下次与您的见面");
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入有误!请重新输入!");
            }
        }

    }

    //个人主页
    public static void personPage(Socket socket) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            CommonUser.Common(user1.getUsername());
            while (true) {
                String choice_P = scanner.next();
                switch (choice_P) {
                    case "1":  //查看帖子，进入论坛主页
                        CommonUser.CheckPosts(user1.getUsername());
                        break;
                    case "2":  //修改密码
                        user1.setPassword(CommonUser.AlterPassWord(user1.getUsername(), user1.getPassword()));
                        String s = "修改密码成功";
                        objectOutputStream.writeObject(s);
                        personPage(socket);
                        break;
                    case "3":  //查看或删除自己的帖子
                        CommonUser.MyPosts(user1.getUsername());
                        personPage(socket);
                        break;
                    case "0":  //退出
                        System.out.println("期待下次与您见面");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("输入错误，请重新输入！！！");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //管理员页面
    public static void adminPage(Socket socket) throws IOException, SQLException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        Administrator.Page(user1.getUsername());
        while (true) {
            String choice_a = scanner.next();
            switch (choice_a) {
                case "001"://论坛主页
                    Forum forum = new Forum();
                    forum.ForumMain(user1.getUsername());
                    break;
                case "002"://秘密树洞
                    Tree tree = new Tree();
                    tree.Secret(user1.getUsername());
                    break;
                case "003"://查看精选帖子
                    Select select = new Select();
                    select.Classy(user1.getUsername());
                    break;
                case "004"://修改密码
                    user1.setPassword(Administrator.AlterPassWord(user1.getUsername(), user1.getPassword()));
                    adminPage(socket);
                    break;
                case "005"://查看或删除自己的帖子
                    Administrator.CheckMyPosts(user1.getUsername());
                    adminPage(socket);
                    break;
                case "006": //删除帖子
                    Administrator.deletePost();
                    adminPage(socket);
                    break;
                case "007": //加入精选
                    Administrator.selectPost();
                    adminPage(socket);
                    break;
                case "008"://退出
                    System.out.println("感谢使用校园论坛,再见！！！");
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入有误，请重新输入！！！");
                    break;
            }
        }
    }


}
