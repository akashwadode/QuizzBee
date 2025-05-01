package com.quizzbee.pages.components;

import com.quizzbee.database.DatabaseManager;
import com.quizzbee.models.Attempt;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ProfileComponent {
    private final int userId;
    private final DatabaseManager dbManager;
    private final VBox contentPane;

    public ProfileComponent(int userId, VBox contentPane) {
        this.userId = userId;
        this.dbManager = new DatabaseManager();
        this.contentPane = contentPane;
    }

    public void show() {
        contentPane.getChildren().clear();
        String username = dbManager.getUsername(userId);
        double accuracyPercentage = dbManager.getAccuracyPercentage(userId);

        VBox profileBox = new VBox(15);
        profileBox.setId("profile-box");
        profileBox.setAlignment(Pos.CENTER);

        var imageUrl = getClass().getResource("/com/quizzbee/images/blank_profile.png");
        ImageView profileImage = new ImageView(imageUrl != null
                ? new Image(imageUrl.toExternalForm())
                : null);
        profileImage.setFitWidth(100);
        profileImage.setFitHeight(100);
        profileImage.setClip(new javafx.scene.shape.Circle(50, 50, 50));

        Label usernameLabel = new Label("Username: " + (username != null ? username : "Unknown"));
        Label accuracyLabel = new Label(String.format("Accuracy: %.2f%%", accuracyPercentage));

        // Create TableView for attempts
        TableView<Attempt> attemptsTable = new TableView<>();
        attemptsTable.setId("attempts-table");
        attemptsTable.setMaxHeight(200);

        TableColumn<Attempt, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoryName()));
        categoryColumn.setPrefWidth(200);

        TableColumn<Attempt, Number> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getScore()));
        scoreColumn.setPrefWidth(100);

        TableColumn<Attempt, Number> totalQuestionsColumn = new TableColumn<>("Total Questions");
        totalQuestionsColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getTotalQuestions()));
        totalQuestionsColumn.setPrefWidth(150);

        attemptsTable.getColumns().addAll(categoryColumn, scoreColumn, totalQuestionsColumn);
        attemptsTable.setItems(javafx.collections.FXCollections.observableArrayList(dbManager.getUserAttempts(userId)));

        attemptsTable.setPlaceholder(new Label("No attempts recorded."));

        ScrollPane tableScrollPane = new ScrollPane(attemptsTable);
        tableScrollPane.setFitToWidth(true);
        tableScrollPane.setFitToHeight(true);
        tableScrollPane.setMaxHeight(220);
        tableScrollPane.setStyle("-fx-background-color: transparent;");

        profileBox.getChildren().addAll(
                createTitleLabel("Profile"),
                profileImage,
                usernameLabel,
                accuracyLabel,
                createTitleLabel("Quiz Attempts"),
                tableScrollPane
        );

        contentPane.getChildren().add(profileBox);
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("title-label");
        return label;
    }
}