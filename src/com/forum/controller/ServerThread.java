package com.forum.controller;



import com.forum.pojo.User;
import com.forum.service.LoginForum;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread implements Runnable {
    private Socket socket;
    private static final ConcurrentHashMap<Socket, String> map = new ConcurrentHashMap<>();

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                loginup(socket);
            }
        } catch (Exception e) {
            String currentThreadName = map.get(socket);
            map.remove(socket);
        }
    }
    //登录界面处理
    private void loginup(Socket socket) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        String choice = null;
        tag:
        while (true) {
            try {
                //获得用户传输的选项
                choice = (String) objectInputStream.readObject();
                if (choice != null) {
                    switch (choice) {
                        case "1":  //注册
                            User user = (User) objectInputStream.readObject();//接收登录的用户输入的信息
                            int flag = LoginForum.add_register(user.getUsername(), user.getPassword());
                            String message = "";
                            switch (flag) {
                                case 1:
                                    message = "注册失败，账号已存在";
                                    break;
                                case 2:
                                    message = "用户名过短或过长";
                                    break;
                                case 3:
                                    message = "密码过短或过长";
                                    break;
                                case 4:
                                    message = "注册成功";
                                    break;
                            }
                            if (flag == 4) {
                                System.out.println("新用户：" + user.getUsername() + "注册成功");
                            }
                            objectOutputStream.writeObject(message);
                            break;

                        case "2":   //登录
                            User user2 = (User) objectInputStream.readObject();   //接收登录的用户输入的信息
                            if (map.containsValue(user2.getUsername())) {
                                objectOutputStream.writeObject(true);
                            } else {
                                int count = 0;
                                objectOutputStream.writeObject(false);
                                count = LoginForum.isLogin(user2.getUsername(), user2.getPassword());
                                objectOutputStream.writeObject(count);
                                if (count != 0) {
                                    map.put(socket, user2.getUsername());
                                    System.out.println("用户：" + user2.getUsername() + "上线");
                                    break tag;
                                }
                            }
                        case "0": //退出
                            String getMessage = "";
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
    //个人页面处理
    private void commuser(Socket socket){

        //修改密码显示
        System.out.println("用户"+map.values()+"修改密码");
    }
}