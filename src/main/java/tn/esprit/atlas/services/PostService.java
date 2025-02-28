package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Post;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    private static Connection connection;

    public PostService() {
        this.connection = DatabaseConnection.getInstance().getCnx();
    }

    // ➤ Add Post
    public void addPost(Post post) {
        String query = "INSERT INTO forum (title, content, created_at, updated_at, viewcount, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";  // Added category_id for the relationship

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getFullContent());  // Assuming full_content is used in the content field
            preparedStatement.setTimestamp(3, new Timestamp(post.getCreatedAt().getTime()));
            preparedStatement.setTimestamp(4, new Timestamp(post.getUpdatedAt().getTime()));
            preparedStatement.setInt(5, post.getViewCount());  // Assuming viewCount is an attribute of the Post
            preparedStatement.setInt(6, post.getCategoryId());  // Assuming categoryId is set in the Post object

            preparedStatement.executeUpdate();
            System.out.println("✅ Post added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add post to the database.", e);
        }
    }

    // ➤ Get All Posts
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM forum";  // Corrected to 'forum' table

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("post_id"));  // Corrected column name to 'post_id'
                post.setTitle(resultSet.getString("title"));
                post.setFullContent(resultSet.getString("content"));
                post.setCreatedAt(resultSet.getTimestamp("created_at"));
                post.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                post.setViewCount(resultSet.getInt("viewcount"));
                post.setCategoryId(resultSet.getInt("category_id"));  // Corrected column name

                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    // ➤ Update Post
    public void updatePost(Post post) {
        String query = "UPDATE forum SET title = ?, content = ?, updated_at = ?, viewcount = ?, category_id = ? WHERE post_id = ?";  // Corrected query to use post_id

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getFullContent());  // Corrected to use full_content
            preparedStatement.setTimestamp(3, new Timestamp(post.getUpdatedAt().getTime()));
            preparedStatement.setInt(4, post.getViewCount());  // Corrected to use viewCount
            preparedStatement.setInt(5, post.getCategoryId());  // Corrected to use categoryId
            preparedStatement.setInt(6, post.getId());  // Corrected to use post_id in WHERE clause

            preparedStatement.executeUpdate();
            System.out.println("✅ Post updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Delete Post
    public void deletePost(int postId) {
        String query = "DELETE FROM forum WHERE post_id = ?";  // Corrected to use post_id

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
            System.out.println("✅ Post deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Find Post by ID
    public Post findPostById(int postId) {
        String query = "SELECT * FROM forum WHERE post_id = ?";  // Corrected to use post_id

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("post_id"));  // Corrected column name to 'post_id'
                post.setTitle(resultSet.getString("title"));
                post.setFullContent(resultSet.getString("content"));
                post.setCreatedAt(resultSet.getTimestamp("created_at"));
                post.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                post.setViewCount(resultSet.getInt("viewcount"));
                post.setCategoryId(resultSet.getInt("category_id"));  // Corrected column name

                return post;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ➤ Get Posts by Category (Join with Category)
    public List<Post> getPostsByCategory(int categoryId) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT p.* FROM forum p WHERE p.category_id = ?";  // Simplified the query since we are only interested in forum posts.

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("post_id"));  // Corrected column name to 'post_id'
                post.setTitle(resultSet.getString("title"));
                post.setFullContent(resultSet.getString("content"));
                post.setCreatedAt(resultSet.getTimestamp("created_at"));
                post.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                post.setViewCount(resultSet.getInt("viewcount"));
                post.setCategoryId(resultSet.getInt("category_id"));

                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}

