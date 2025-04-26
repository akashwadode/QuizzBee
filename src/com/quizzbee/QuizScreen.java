package com.quizzbee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class QuizScreen {
    private int userId;
    private int categoryId;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Label questionLabel;
    private RadioButton optionA, optionB, optionC, optionD;
    private ToggleGroup optionsGroup;
    private Button nextButton;
    private DatabaseManager dbManager = new DatabaseManager();
    private Stage primaryStage;

    public QuizScreen(int userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
        loadQuestions();
    }

    public void show(Stage stage) {
        this.primaryStage = stage;
        questionLabel = new Label();
        optionsGroup = new ToggleGroup();
        optionA = new RadioButton();
        optionB = new RadioButton();
        optionC = new RadioButton();
        optionD = new RadioButton();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);

        nextButton = new Button("Next");
        nextButton.setOnAction(e -> handleNextButton());

        VBox layout = new VBox(10, questionLabel, optionA, optionB, optionC, optionD, nextButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("QuizzBee - Quiz");
        primaryStage.setScene(scene);
        displayQuestion();
    }

    private void loadQuestions() {
        questions = dbManager.loadQuestions(categoryId);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText(q.getQuestionText());
            optionA.setText(q.getOptionA());
            optionB.setText(q.getOptionB());
            optionC.setText(q.getOptionC());
            optionD.setText(q.getOptionD());
            optionsGroup.selectToggle(null);
            nextButton.setText("Submit");
        } else {
            if (userId > 0) {
                dbManager.saveAttempt(userId, categoryId, score, questions.size());
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid user ID. Attempt not saved.").showAndWait();
            }
            showResults();
        }
    }

    private void handleNextButton() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
            if (selected != null) {
                String selectedAnswer = selected.getText().equals(q.getOptionA()) ? "A" :
                        selected.getText().equals(q.getOptionB()) ? "B" :
                                selected.getText().equals(q.getOptionC()) ? "C" : "D";
                if (selectedAnswer.equals(q.getCorrectAnswer())) {
                    score++;
                }
                currentQuestionIndex++;
                displayQuestion();
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select an option!").showAndWait();
            }
        }
    }

    private void showResults() {
        questionLabel.setText("Quiz Completed! Your Score: " + score + "/" + questions.size());
        optionA.setVisible(false);
        optionB.setVisible(false);
        optionC.setVisible(false);
        optionD.setVisible(false);
        nextButton.setText("Home");
        nextButton.setOnAction(e -> {
            currentQuestionIndex = 0;
            score = 0;
            DashboardScreen dashboardScreen = new DashboardScreen(userId);
            dashboardScreen.show(primaryStage);
        });
    }
}