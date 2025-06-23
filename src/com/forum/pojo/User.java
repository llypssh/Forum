package com.forum.pojo;

import javax.xml.namespace.QName;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private int id;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "用户名='" + username + '\'' +
                ", 密码='" + password + '\'' +
                ", 用户id=" + id +
                '}';
    }
}
