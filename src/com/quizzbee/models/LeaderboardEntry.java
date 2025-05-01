package com.quizzbee.models;

public class LeaderboardEntry {
    private final String username;
    private final double accuracy;

    public LeaderboardEntry(String username, double accuracy) {
        this.username = username;
        this.accuracy = accuracy;
    }

    public String getUsername() { return username; }
    public double getAccuracy() { return accuracy; }
}