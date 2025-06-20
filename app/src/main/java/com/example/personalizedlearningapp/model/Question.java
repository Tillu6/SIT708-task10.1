package com.example.personalizedlearningapp.model;

import java.io.Serializable;

public class Question implements Serializable {
    public String questionTitle; // e.g. "What is recursion?"
    public String[] options;
    public int selectedOption = -1;
    public String correctAnswer;
    public String response; // For result page display
    public boolean isExpanded = false; // Control expansion status

    
    public String timestamp;
    public String taskTitle;

    public Question(String questionTitle, String[] options) {
        this.questionTitle = questionTitle;
        this.options = options;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public String getAnswer() {
        return correctAnswer;
    }
}
