package com.quizzbee.pages.components;


import com.quizzbee.database.DatabaseManager;
import com.quizzbee.models.LeaderboardEntry;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class LeaderboardComponent {
    private final VBox contentPane;
    private final DatabaseManager dbManager;

    public LeaderboardComponent(VBox contentPane) {
        this.contentPane = contentPane;
        this.dbManager = new DatabaseManager();
    }

    public void show() {
        contentPane.getChildren().clear();

        VBox leaderboardLayout = new VBox(10);
        leaderboardLayout.setAlignment(Pos.CENTER);
        leaderboardLayout.setPadding(new Insets(20));
        leaderboardLayout.setId("leaderboard-box");

        TableView<LeaderboardEntry> table = new TableView<>();
        table.setId("leaderboard-table");

        TableColumn<LeaderboardEntry, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        usernameCol.setPrefWidth(200);

        TableColumn<LeaderboardEntry, Number> scoreCol = new TableColumn<>("Total Score");
        scoreCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()));
        scoreCol.setPrefWidth(150);

        table.getColumns().addAll(usernameCol, scoreCol);
        table.setPlaceholder(new Label("No leaderboard data available."));

        dbManager.loadLeaderboardData(table);

        leaderboardLayout.getChildren().addAll(createTitleLabel("Leaderboard"), table);
        contentPane.getChildren().add(leaderboardLayout);
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("title-label");
        return label;
    }
}