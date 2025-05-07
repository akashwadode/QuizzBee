package com.quizzbee.database;

import com.quizzbee.models.Attempt;
import com.quizzbee.models.LeaderboardEntry;
import com.quizzbee.models.Question;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_db?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "primeaj@25";
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
        lastLoggedInUserId = -1;
        return -1;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    lastLoggedInUserId = generatedKeys.getInt(1);
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + username);
            e.printStackTrace();
            return false;
        }
    }

    public String getUsername(int userId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT username FROM users WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public double getAccuracyPercentage(int userId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT SUM(score) as total_score, SUM(total_questions) as total_questions " +
                             "FROM attempts WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int totalScore = rs.getInt("total_score");
                int totalQuestions = rs.getInt("total_questions");
                if (totalQuestions > 0) {
                    return (double) totalScore / totalQuestions * 100.0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error calculating accuracy for userId: " + userId);
            e.printStackTrace();
        }
        return 0.0;
    }

    public void setLastLoggedInUserId(int userId) {
        this.lastLoggedInUserId = userId;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT category_name FROM categories")) {
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
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

    public boolean saveAttempt(int userId, int categoryId, int score, int totalQuestions) {
        if (userId <= 0) {
            System.err.println("Invalid userId: " + userId);
            return false;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO attempts (user_id, category_id, score, total_questions) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, score);
            stmt.setInt(4, totalQuestions);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving attempt for userId: " + userId + ", categoryId: " + categoryId);
            e.printStackTrace();
            return false;
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

    public List<Attempt> getUserAttempts(int userId) {
        List<Attempt> attempts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.category_name, a.score, a.total_questions " +
                             "FROM attempts a JOIN categories c ON a.category_id = c.category_id " +
                             "WHERE a.user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attempts.add(new Attempt(
                        rs.getString("category_name"),
                        rs.getInt("score"),
                        rs.getInt("total_questions")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching attempts for userId: " + userId);
        }
        return attempts;
    }
}