package com.jgcheca.kiuwandroid.Utils;

/**
 * Created by asus on 15/06/2015.
 */
public class Session {
    private static Session yo;
    private String password;
    private String username;
    private String sessionId;
    private String authorization;

    private Session() {

    }

    public static Session get() {
        if (yo == null)
            yo = new Session();
        return yo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getAuthorization() {
        return authorization;
    }
}
