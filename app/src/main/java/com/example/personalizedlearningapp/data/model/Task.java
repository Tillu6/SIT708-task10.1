package com.example.personalizedlearningapp.data.model;

import java.util.List;

public class Task {
    private String id;
    private String title;
    private String description;
    private List<String> questions;

    public Task(String id, String title, String description, List<String> questions) {
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.questions   = questions;
    }
    // getters/setters…
}
