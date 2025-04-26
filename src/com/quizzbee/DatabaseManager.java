package com.quizzbee;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_db?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "primeaj@25"; // Replace with your MySQL password
    private int lastLoggedInUserId = -1;

    public int authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id, password FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    lastLoggedInUserId = rs.getInt("user_id");
                    return lastLoggedInUserId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getLastLoggedInUserId() {
        return lastLoggedInUserId;
    }

    public void loadCategories(ComboBox<String> comboBox) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT category_name FROM categories")) {
            while (rs.next()) {
                comboBox.getItems().add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCategoryId(String categoryName) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT category_id FROM categories WHERE category_name = ?")) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Question> loadQuestions(int categoryId) {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE category_id = ?")) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void saveAttempt(int userId, int categoryId, int score, int totalQuestions) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO attempts (user_id, category_id, score, total_questions) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, score);
            stmt.setInt(4, totalQuestions);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadLeaderboardData(TableView<LeaderboardEntry> table) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT u.username, SUM(a.score) as total_score " +
                             "FROM users u " +
                             "LEFT JOIN attempts a ON u.user_id = a.user_id " +
                             "GROUP BY u.user_id, u.username " +
                             "ORDER BY total_score DESC")) {
            while (rs.next()) {
                table.getItems().add(new LeaderboardEntry(
                        rs.getString("username"),
                        rs.getInt("total_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}