package com.quizzbee.models;

public class Attempt {
    private String categoryName;
    private int score;
    private int totalQuestions;

    public Attempt(String categoryName, int score, int totalQuestions) {
        this.categoryName = categoryName;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }
}