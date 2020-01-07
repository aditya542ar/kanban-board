package me.ad.kanban.auth;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private final String jwtToken;
    private final String userId;

    public LoginResponse(String userId, String jwtToken) {
        this.userId = userId;
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUserId() {
        return userId;
    }
}
