module com.quizzbee {
    requires javafx.controls;
    requires java.sql; // For MySQL
    exports com.quizzbee;
    exports com.quizzbee.database;
    exports com.quizzbee.pages;
    exports com.quizzbee.models;
}