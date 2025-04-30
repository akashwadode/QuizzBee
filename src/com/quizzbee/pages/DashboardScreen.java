package com.quizzbee.pages;

import com.quizzbee.database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class DashboardScreen {
    private final int userId;
    private final DatabaseManager dbManager = new DatabaseManager();
    private VBox contentPane;
    private VBox selectedBox;
    private String selectedCategory;

    public DashboardScreen(int userId) {
        this.userId = userId;
    }

    public void show(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();
        VBox sidebar = createSidebar(primaryStage);
        mainLayout.setLeft(sidebar);

        contentPane = new VBox();
        contentPane.setId("content-pane");
        mainLayout.setCenter(contentPane);

        showCategoriesContent();

        Scene dashboardScene = new Scene(mainLayout, 800, 500);

        var cssUrl = getClass().getResource("/com/quizzbee/styles/dashboard.css");
        if (cssUrl != null) {
            dashboardScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("dashboard.css not found at /com/quizzbee/styles/dashboard.css");
        }

        primaryStage.setTitle("QuizzBee - Dashboard");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }

    private VBox createSidebar(Stage primaryStage) {
        VBox sidebar = new VBox();
        sidebar.setId("sidebar");

        Button categoriesButton = createSidebarButton("Quiz", this::showCategoriesContent);
        Button leaderboardButton = createSidebarButton("Leaderboard", () -> {
            new LeaderboardScreen().show(primaryStage);
        });
        Button profileButton = createSidebarButton("Profile", this::showProfileContent);
        Button aboutButton = createSidebarButton("About Us", this::showAboutUsContent);
        Button logoutButton = createSidebarButton("Logout", () -> {
            dbManager.setLastLoggedInUserId(-1);
            new LoginScreen().show(primaryStage);
        });

        sidebar.getChildren().addAll(categoriesButton, leaderboardButton, profileButton, aboutButton, logoutButton);
        return sidebar;
    }

    private Button createSidebarButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showProfileContent() {
        contentPane.getChildren().clear();
        String username = dbManager.getUsername(userId);
        int totalScore = dbManager.getTotalScore(userId);

        VBox profileBox = new VBox();
        profileBox.setId("profile-box");

        var imageUrl = getClass().getResource("/com/quizzbee/images/blank_profile.png");
        ImageView profileImage = new ImageView(imageUrl != null
                ? new Image(imageUrl.toExternalForm())
                : null);
        profileImage.setFitWidth(100);
        profileImage.setFitHeight(100);
        profileImage.setClip(new javafx.scene.shape.Circle(50, 50, 50));

        Label usernameLabel = new Label("Username: " + (username != null ? username : "Unknown"));
        Label scoreLabel = new Label("Total Score: " + totalScore);

        profileBox.getChildren().addAll(
                createTitleLabel("Profile"),
                profileImage,
                usernameLabel,
                scoreLabel
        );

        contentPane.getChildren().add(profileBox);
    }

    private void showAboutUsContent() {
        contentPane.getChildren().clear();

        VBox aboutBox = new VBox();
        aboutBox.setId("about-box");
        aboutBox.getChildren().addAll(
                createTitleLabel("About Us"),
                new Label("QuizzBee is an interactive quiz application designed to test your knowledge " +
                        "across categories like Maths, Logical Reasoning, English, Computer Science, and AI.\n" +
                        "Compete with others, track your progress, and have fun learning!")
        );

        contentPane.getChildren().add(aboutBox);
    }

    private void showCategoriesContent() {
        contentPane.getChildren().clear();
        Label titleLabel = createTitleLabel("Select a Category");

        FlowPane categoryFlow = new FlowPane();
        categoryFlow.setId("category-flow");

        List<String> categories = dbManager.getAllCategories();

        for (String category : categories) {
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.getStyleClass().add("category-box");

            Label label = new Label(category);
            label.getStyleClass().add("category-label");
            box.getChildren().add(label);

            box.setOnMouseClicked(e -> {
                if (selectedBox != null) {
                    selectedBox.getStyleClass().remove("category-box-selected");
                }

                box.getStyleClass().add("category-box-selected");
                selectedBox = box;
                selectedCategory = category;
                System.out.println("Selected category: " + category);
            });

            categoryFlow.getChildren().add(box);
        }

        Button startQuizButton = new Button("Start Quiz");
        startQuizButton.getStyleClass().add("start-quiz-button");
        startQuizButton.setOnAction(e -> {
            if (selectedCategory != null) {
                int categoryId = dbManager.getCategoryId(selectedCategory);
                if (categoryId > 0) {
                    new QuizScreen(userId, categoryId).show((Stage) contentPane.getScene().getWindow());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid category selected.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Please select a category!");
            }
        });

        VBox wrapper = new VBox(titleLabel, categoryFlow, startQuizButton);
        wrapper.setId("category-box");

        contentPane.getChildren().add(wrapper);
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("title-label");
        return label;
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}
