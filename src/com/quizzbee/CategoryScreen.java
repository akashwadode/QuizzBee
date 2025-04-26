package com.quizzbee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryScreen {
    private int userId;
    private DatabaseManager dbManager = new DatabaseManager();

    public CategoryScreen(int userId) {
        this.userId = userId;
    }

    public void show(Stage primaryStage) {
        VBox categoryLayout = new VBox(10);
        categoryLayout.setAlignment(Pos.CENTER);
        categoryLayout.setPadding(new Insets(20));

        Label selectLabel = new Label("Select a Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        dbManager.loadCategories(categoryComboBox);

        Button startQuizButton = new Button("Start Quiz");
        startQuizButton.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory != null) {
                int categoryId = dbManager.getCategoryId(selectedCategory);
                QuizScreen quizScreen = new QuizScreen(userId, categoryId);
                quizScreen.show(primaryStage);
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a category!").showAndWait();
            }
        });

        Button leaderboardButton = new Button("View Leaderboard");
        leaderboardButton.setOnAction(e -> {
            LeaderboardScreen leaderboardScreen = new LeaderboardScreen();
            leaderboardScreen.show(primaryStage);
        });

        categoryLayout.getChildren().addAll(selectLabel, categoryComboBox, startQuizButton, leaderboardButton);
        Scene categoryScene = new Scene(categoryLayout, 400, 300);
        primaryStage.setTitle("QuizzBee - Select Category");
        primaryStage.setScene(categoryScene);
    }
}