package com.forum.controller;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ForumService{
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("校园论坛服务器已启动");
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
