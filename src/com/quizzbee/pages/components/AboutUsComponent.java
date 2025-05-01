package com.quizzbee.pages.components;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AboutUsComponent {
    private final VBox contentPane;

    public AboutUsComponent(VBox contentPane) {
        this.contentPane = contentPane;
    }

    public void show() {
        contentPane.getChildren().clear();

        VBox aboutBox = new VBox();
        aboutBox.setId("about-box");
        aboutBox.getChildren().addAll(
                createTitleLabel("About Us"),
                new Label("QuizzBee is an interactive quiz application designed to test your knowledge " +
                        "across categories like Maths, Logical Reasoning, English, Computer Science, and AI.\n" +
                        "Compete with others, track your progress, and have fun learning!")
        );

        contentPane.getChildren().add(aboutBox);
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("title-label");
        return label;
    }
}