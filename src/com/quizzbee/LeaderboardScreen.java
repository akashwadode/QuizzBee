package com.quizzbee;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeaderboardScreen {
    private DatabaseManager dbManager = new DatabaseManager();

    public void show(Stage primaryStage) {
        VBox leaderboardLayout = new VBox(10);
        leaderboardLayout.setAlignment(Pos.CENTER);
        leaderboardLayout.setPadding(new Insets(20));

        TableView<LeaderboardEntry> table = new TableView<>();
        TableColumn<LeaderboardEntry, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        TableColumn<LeaderboardEntry, Number> scoreCol = new TableColumn<>("Total Score");
        scoreCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()));
        table.getColumns().addAll(usernameCol, scoreCol);

        dbManager.loadLeaderboardData(table);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            CategoryScreen categoryScreen = new CategoryScreen(dbManager.getLastLoggedInUserId());
            categoryScreen.show(primaryStage);
        });

        leaderboardLayout.getChildren().addAll(new Label("Leaderboard"), table, backButton);
        Scene leaderboardScene = new Scene(leaderboardLayout, 600, 400);
        primaryStage.setTitle("QuizzBee - Leaderboard");
        primaryStage.setScene(leaderboardScene);
    }
}