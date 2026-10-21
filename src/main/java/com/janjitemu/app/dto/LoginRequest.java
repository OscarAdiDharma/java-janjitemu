package com.janjitemu.app.dto;

public class LoginRequest {
    private String identifier;
    private String email;
    private String username;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String identifier, String email, String username, String password) {
        this.identifier = identifier;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
