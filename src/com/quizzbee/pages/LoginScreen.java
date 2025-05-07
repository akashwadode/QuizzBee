package com.quizzbee.pages;

import com.quizzbee.database.DatabaseManager;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {
    private final DatabaseManager dbManager = new DatabaseManager();

    public void show(Stage primaryStage) {
        VBox logoPane = new VBox();
        logoPane.setId("logo-pane");

        var logoUrl = getClass().getResource("/com/quizzbee/images/quiz_image.png");
        ImageView logo = new ImageView(logoUrl != null ? new Image(logoUrl.toExternalForm()) : null);
        logo.setFitWidth(340);
        logo.setFitHeight(340);
        logoPane.getChildren().add(logo);

        VBox formPane = new VBox(15);
        formPane.setId("form-pane");

        Label titleLabel = new Label("Welcome to QuizzBee");
        titleLabel.getStyleClass().add("form-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            int userId = dbManager.authenticateUser(username, password);
            if (userId > 0) {
                messageLabel.setText("Login successful!");
                DashboardScreen dashboardScreen = new DashboardScreen(userId);
                dashboardScreen.show(primaryStage);
            } else {
                messageLabel.setText("Invalid username or password.");
                new Alert(Alert.AlertType.ERROR, "Login failed. Please check your credentials.").showAndWait();
            }
        });

        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (dbManager.registerUser(username, password)) {
                messageLabel.setText("Sign up successful! Please log in.");
                usernameField.clear();
                passwordField.clear();
            } else {
                messageLabel.setText("Sign up failed. Username may already exist or input is invalid.");
                new Alert(Alert.AlertType.ERROR, "Sign up failed. Try a different username or check input.").showAndWait();
            }
        });

        formPane.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, signupButton, messageLabel);

        HBox mainLayout = new HBox();
        mainLayout.setId("main-layout");
        HBox.setHgrow(logoPane, Priority.ALWAYS);
        HBox.setHgrow(formPane, Priority.ALWAYS);
        mainLayout.getChildren().addAll(logoPane, formPane);

        Scene loginScene = new Scene(mainLayout, 800, 500);
        var cssUrl = getClass().getResource("/com/quizzbee/styles/login.css");
        if (cssUrl != null) {
            loginScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("login.css not found at /com/quizzbee/styles/login.css");
        }

        primaryStage.setTitle("QuizzBee - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
