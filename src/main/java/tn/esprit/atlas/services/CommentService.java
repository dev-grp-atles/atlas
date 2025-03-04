package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Comment;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentService {

    private static Connection connection;

    public CommentService() {
        this.connection = DatabaseConnection.getInstance().getCnx();
    }

    // ➤ Add Comment
    public void addComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO commentaire (content, post_id, created_at, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getContent());
            statement.setInt(2, comment.getPostId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Comment successfully added to the database.");
            } else {
                System.out.println("Failed to add comment to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while adding comment: " + e.getMessage());
            throw e; // Re-throw the exception to be caught in the UI
        }
    }

    // ➤ Get All Comments by Post ID
    public List<Comment> getAllCommentsByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE post_id = ?";  // Correct table name

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = new Comment(
                            resultSet.getInt("comment_id"),  // Correct column name for primary key
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getInt("post_id")
                    );
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void updateComment(Comment comment) throws SQLException {
        String sql = "UPDATE commentaire SET content = ?, updated_at = CURRENT_TIMESTAMP WHERE comment_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getContent()); // Update the content
            statement.setInt(2, comment.getId()); // Use the comment's ID

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Comment successfully updated.");
            } else {
                System.out.println("Failed to update comment.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while updating comment: " + e.getMessage());
            throw e;
        }
    }


    // ➤ Delete Comment
    public void deleteComment(int commentId) {
        String query = "DELETE FROM commentaire WHERE comment_id = ?";  // Correct column name

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();
            System.out.println("✅ Comment deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Find Comment by ID
    public Comment findCommentById(int commentId) {
        String query = "SELECT * FROM commentaire WHERE comment_id = ?";  // Correct column name
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, commentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Comment(
                            resultSet.getInt("comment_id"),  // Correct column name for primary key
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getInt("post_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
