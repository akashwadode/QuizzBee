package com.quizzbee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    private DatabaseManager dbManager = new DatabaseManager();

    public void show(Stage primaryStage) {
        VBox loginLayout = new VBox(10);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            int userId = dbManager.authenticateUser(username, password);
            if (userId > 0) {
                messageLabel.setText("Login successful!");
                CategoryScreen categoryScreen = new CategoryScreen(userId);
                categoryScreen.show(primaryStage);
            } else {
                messageLabel.setText("Invalid username or password.");
                new Alert(Alert.AlertType.ERROR, "Login failed. Please check your credentials.").showAndWait();
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (dbManager.registerUser(username, password)) {
                messageLabel.setText("Registration successful! Please log in.");
                usernameField.clear();
                passwordField.clear();
            } else {
                messageLabel.setText("Registration failed. Username may already exist or input is invalid.");
                new Alert(Alert.AlertType.ERROR, "Registration failed. Try a different username.").showAndWait();
            }
        });

        loginLayout.getChildren().addAll(usernameField, passwordField, loginButton, registerButton, messageLabel);
        Scene loginScene = new Scene(loginLayout, 400, 300);
        primaryStage.setTitle("QuizzBee - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}