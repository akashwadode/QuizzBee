package com.quizzbee.pages;

import com.quizzbee.database.DatabaseManager;
import com.quizzbee.pages.components.AboutUsComponent;
import com.quizzbee.pages.components.CategoriesComponent;
import com.quizzbee.pages.components.LeaderboardComponent;
import com.quizzbee.pages.components.ProfileComponent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardScreen {
    private final int userId;
    private final DatabaseManager dbManager = new DatabaseManager();
    private VBox contentPane;

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

        // Load Categories component by default
        new CategoriesComponent(userId, primaryStage, contentPane).show();

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

        Button categoriesButton = createSidebarButton("Quiz", () ->
                new CategoriesComponent(userId, primaryStage, contentPane).show());
        Button leaderboardButton = createSidebarButton("Leaderboard", () ->
                new LeaderboardComponent(contentPane).show());
        Button profileButton = createSidebarButton("Profile", () ->
                new ProfileComponent(userId, contentPane).show());
        Button aboutButton = createSidebarButton("About Us", () ->
                new AboutUsComponent(contentPane).show());
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
}