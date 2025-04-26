package com.quizzbee.pages;

import com.quizzbee.database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardScreen {
    private int userId;
    private DatabaseManager dbManager = new DatabaseManager();
    private VBox contentPane;

    public DashboardScreen(int userId) {
        this.userId = userId;
    }

    public void show(Stage primaryStage) {
        // Main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Navbar
        HBox navbar = new HBox(10);
        navbar.setPadding(new Insets(10));
        navbar.setStyle("-fx-background-color: #333; -fx-alignment: center;");

        Button profileButton = new Button("Profile");
        Button aboutButton = new Button("About Us");
        Button categoriesButton = new Button("Categories");
        Button leaderboardButton = new Button("Leaderboard");
        Button logoutButton = new Button("Logout");

        // Style buttons
        for (Button btn : new Button[]{profileButton, aboutButton, categoriesButton, leaderboardButton, logoutButton}) {
            btn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #777; -fx-text-fill: white; -fx-font-size: 14px;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px;"));
        }

        navbar.getChildren().addAll(profileButton, aboutButton, categoriesButton, leaderboardButton, logoutButton);

        // Content pane for dynamic content
        contentPane = new VBox(10);
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setPadding(new Insets(20));

        // Default content: Categories
        showCategoriesContent();

        // Navbar actions
        profileButton.setOnAction(e -> showProfileContent());
        aboutButton.setOnAction(e -> showAboutUsContent());
        categoriesButton.setOnAction(e -> showCategoriesContent());
        leaderboardButton.setOnAction(e -> {
            LeaderboardScreen leaderboardScreen = new LeaderboardScreen();
            leaderboardScreen.show(primaryStage);
        });
        logoutButton.setOnAction(e -> {
            dbManager.setLastLoggedInUserId(-1); // Clear session
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(primaryStage);
        });

        mainLayout.getChildren().addAll(navbar, contentPane);
        Scene dashboardScene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("QuizzBee - Dashboard");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }

    private void showProfileContent() {
        contentPane.getChildren().clear();
        String username = dbManager.getUsername(userId);
        int totalScore = dbManager.getTotalScore(userId);
        Label profileLabel = new Label("Profile");
        profileLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label usernameLabel = new Label("Username: " + (username != null ? username : "Unknown"));
        Label scoreLabel = new Label("Total Score: " + totalScore);
        contentPane.getChildren().addAll(profileLabel, usernameLabel, scoreLabel);
    }

    private void showAboutUsContent() {
        contentPane.getChildren().clear();
        Label aboutLabel = new Label("About Us");
        aboutLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label aboutText = new Label("QuizzBee is an interactive quiz application designed to test your knowledge " +
                "across various categories like Maths, Logical Reasoning, English, Computer Science, and AI. " +
                "Compete with others, track your progress, and have fun learning!");
        aboutText.setWrapText(true);
        contentPane.getChildren().addAll(aboutLabel, aboutText);
    }

    private void showCategoriesContent() {
        contentPane.getChildren().clear();
        Label selectLabel = new Label("Select a Category:");
        selectLabel.setStyle("-fx-font-size: 16px;");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        dbManager.loadCategories(categoryComboBox);
        if (!categoryComboBox.getItems().isEmpty()) {
            categoryComboBox.setValue(categoryComboBox.getItems().get(0));
        }

        Button startQuizButton = new Button("Start Quiz");
        startQuizButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startQuizButton.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory != null) {
                int categoryId = dbManager.getCategoryId(selectedCategory);
                if (categoryId > 0) {
                    QuizScreen quizScreen = new QuizScreen(userId, categoryId);
                    quizScreen.show((Stage) contentPane.getScene().getWindow());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid category selected.").showAndWait();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a category!").showAndWait();
            }
        });

        contentPane.getChildren().addAll(selectLabel, categoryComboBox, startQuizButton);
    }
}