package me.ad.kanban.auth;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private String userName;
    private String password;

    // Default Constructor
    public LoginRequest() {

    }

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
