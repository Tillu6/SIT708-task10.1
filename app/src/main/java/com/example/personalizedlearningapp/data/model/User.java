package com.example.personalizedlearningapp.data.model;

import java.util.List;

public class User {
    private String username;
    private String email;
    private List<String> interests;

    public User(String username, String email, List<String> interests) {
        this.username   = username;
        this.email      = email;
        this.interests  = interests;
    }
    // getters/setters…
}
