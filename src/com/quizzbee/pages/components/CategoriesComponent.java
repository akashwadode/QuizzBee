package com.quizzbee.pages.components;

import com.quizzbee.database.DatabaseManager;
import com.quizzbee.pages.QuizScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CategoriesComponent {
    private final int userId;
    private final Stage primaryStage;
    private final VBox contentPane;
    private final DatabaseManager dbManager;
    private VBox selectedBox;
    private String selectedCategory;

    public CategoriesComponent(int userId, Stage primaryStage, VBox contentPane) {
        this.userId = userId;
        this.primaryStage = primaryStage;
        this.contentPane = contentPane;
        this.dbManager = new DatabaseManager();
    }

    public void show() {
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
                    new QuizScreen(userId, categoryId).show(primaryStage);
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