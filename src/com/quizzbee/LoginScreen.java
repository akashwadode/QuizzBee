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
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            int userId = dbManager.authenticateUser(username, password);
            if (userId != -1) {
                messageLabel.setText("Login successful!");
                CategoryScreen categoryScreen = new CategoryScreen(userId);
                categoryScreen.show(primaryStage);
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        loginLayout.getChildren().addAll(usernameField, passwordField, loginButton, messageLabel);
        Scene loginScene = new Scene(loginLayout, 400, 300);
        primaryStage.setTitle("QuizzBee - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}