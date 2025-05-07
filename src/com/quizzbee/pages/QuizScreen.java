package com.quizzbee.pages;

import com.quizzbee.models.Question;
import com.quizzbee.database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuizScreen {
    private final int userId;
    private final int categoryId;
    private final List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Label questionLabel;
    private Label questionCounterLabel;
    private RadioButton optionA, optionB, optionC, optionD;
    private ToggleGroup optionsGroup;
    private Button nextButton;
    private final DatabaseManager dbManager = new DatabaseManager();
    private Stage primaryStage;

    public QuizScreen(int userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
        loadQuestions();
    }

    public void show(Stage stage) {
        this.primaryStage = stage;

        questionCounterLabel = new Label();
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
        nextButton = new Button("Submit");
        nextButton.setOnAction(e -> handleNextButton());

        VBox quizLayout = new VBox(15, questionCounterLabel, questionLabel, optionA, optionB, optionC, optionD, nextButton);
        quizLayout.setAlignment(Pos.CENTER_LEFT);
        quizLayout.setPadding(new Insets(20, 40, 20, 40));
        quizLayout.setStyle("-fx-background-color: #f5f5f5;");

        Scene scene = new Scene(quizLayout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/com/quizzbee/styles/quiz.css").toExternalForm());
        primaryStage.setTitle("QuizzBee - Quiz");
        primaryStage.setScene(scene);

        // Display first question or error if no questions
        if (questions.isEmpty()) {
            showError("No questions available for this category.");
        } else {
            displayQuestion();
        }
    }

    private void loadQuestions() {
        List<Question> allQuestions = dbManager.loadQuestions(categoryId);
        Collections.shuffle(allQuestions); // Randomize the order of questions
        int maxQuestions = Math.min(allQuestions.size(), 10); // Limit to 10 questions
        questions.addAll(allQuestions.subList(0, maxQuestions)); // Add up to 10 questions
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionCounterLabel.setText(String.format("Question %d of %d", currentQuestionIndex + 1, questions.size()));
            questionLabel.setText(question.getQuestionText());
            optionA.setText(question.getOptionA());
            optionB.setText(question.getOptionB());
            optionC.setText(question.getOptionC());
            optionD.setText(question.getOptionD());
            optionsGroup.selectToggle(null);
            nextButton.setText("Submit");
        } else {
            saveAttempt();
            showResults();
        }
    }

    private void handleNextButton() {
        if (currentQuestionIndex >= questions.size()) {
            return;
        }

        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected == null) {
            showWarning("Please select an option!");
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        if (isCorrectAnswer(selected, question)) {
            score++;
        }

        currentQuestionIndex++;
        displayQuestion();
    }

    private boolean isCorrectAnswer(RadioButton selected, Question question) {
        String selectedAnswer = selected.getText().equals(question.getOptionA()) ? "A" :
                selected.getText().equals(question.getOptionB()) ? "B" :
                        selected.getText().equals(question.getOptionC()) ? "C" : "D";
        return selectedAnswer.equals(question.getCorrectAnswer());
    }

    private void saveAttempt() {
        if (userId <= 0) {
            showError("Invalid user ID. Attempt not saved.");
            return;
        }
        dbManager.saveAttempt(userId, categoryId, score, questions.size());
    }

    private void showResults() {
        questionCounterLabel.setText("");
        questionLabel.setText(String.format("Quiz Completed! Your Score: %d/%d", score, questions.size()));
        optionA.setVisible(false);
        optionB.setVisible(false);
        optionC.setVisible(false);
        optionD.setVisible(false);
        nextButton.setText("Back to Dashboard");
        nextButton.setOnAction(e -> confirmAndReturnToDashboard());
    }

    private void confirmAndReturnToDashboard() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Return to dashboard?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            currentQuestionIndex = 0;
            score = 0;
            DashboardScreen dashboardScreen = new DashboardScreen(userId);
            dashboardScreen.show(primaryStage);
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}