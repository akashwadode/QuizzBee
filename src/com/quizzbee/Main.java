package com.quizzbee;

import com.quizzbee.database.DatabaseManager;
import com.quizzbee.pages.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager dbManager = new DatabaseManager();
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.show(primaryStage);
    }
}
