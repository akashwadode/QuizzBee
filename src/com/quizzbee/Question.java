package com.quizzbee;

public class Question {
    private final int id;
    private final String questionText;
    private final String optionA, optionB, optionC, optionD;
    private final String correctAnswer;

    public Question(int id, String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
}