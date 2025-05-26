
package com.example.personalizedlearningapp.model;

import java.io.Serializable;

public class Task implements Serializable {
    public String title;
    public String description;
    public String status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = "";
    }
}