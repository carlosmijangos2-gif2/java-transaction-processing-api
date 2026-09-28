package com.example.demo.transactionprocessing.dto;

public class LoginResponse {

    private boolean authenticated;
    private String message;

    public LoginResponse(boolean authenticated, String message) {
        this.authenticated = authenticated;
        this.message = message;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getMessage() {
        return message;
    }
}