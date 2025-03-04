package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Review;
import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewService implements IService<Review> {

    private Connection connection;

    public ReviewService() {
        connection = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void add(Review review) {
        String query = "INSERT INTO Review (reviewer_name, comment, rating, hotel_id, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, review.getReviewerName());
            stmt.setString(2, review.getComment());
            stmt.setFloat(3, review.getRating());  // Ensure the correct data type
            stmt.setInt(4, review.getHotelId());

            // Check if the User object is properly set
            if (review.getUser() != null) {
                stmt.setInt(5, review.getUser().getId());  // Set the user_id for the review
            } else {
                System.err.println("User is not set for the review.");
                // Handle this case as needed, e.g., throw an exception or set a default user_id
                stmt.setInt(5, 1);  // You can set a default user_id here if necessary
            }

            stmt.executeUpdate();

            // Retrieve the auto-generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Review added successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to add review: " + e.getMessage());
        }
    }

    @Override
    public void update(Review review) {
        String query = "UPDATE Review SET comment = ?, rating = ?, user_id = ?, hotel_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, review.getComment());
            stmt.setFloat(2, review.getRating());  // Changed from setInt to setFloat
            stmt.setInt(3, review.getUser().getId());
            stmt.setInt(4, review.getHotelId());
            stmt.setInt(5, review.getId());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Review updated successfully!");
            } else {
                System.out.println("No review found with ID: " + review.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to update review: " + e.getMessage());
        }
    }

    @Override
    public void delete(Review review) {
        String query = "DELETE FROM Review WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, review.getId());
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Review deleted successfully!");
            } else {
                System.out.println("No review found with ID: " + review.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete review: " + e.getMessage());
        }
    }

    @Override
    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.* FROM Review r JOIN utilisateur u ON r.user_id = u.id";  // Changed 'User' to 'utilisateur'
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("adresse"),
                        rs.getString("role"),
                        rs.getString("profileImage"),
                        rs.getString("num_telph"),
                        rs.getString("voyageurPreferences"),
                        rs.getString("destinations_preferrees"),
                        rs.getDouble("budget")
                );
                Review review = new Review(
                        rs.getInt("id"),
                        rs.getString("comment"),
                        rs.getFloat("rating"),  // Changed from getInt to getFloat
                        user,
                        rs.getInt("hotel_id")
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch reviews: " + e.getMessage());
        }
        return reviews;
    }

    @Override
    public Review getOne() {
        throw new UnsupportedOperationException("getOne() method is not implemented.");
    }

    public Review getById(int id) {
        Review review = null;
        String query = "SELECT r.*, u.* FROM Review r JOIN utilisateur u ON r.user_id = u.id WHERE r.id = ?";  // Changed 'User' to 'utilisateur'
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getInt("age"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("adresse"),
                            rs.getString("role"),
                            rs.getString("profileImage"),
                            rs.getString("num_telph"),
                            rs.getString("voyageurPreferences"),
                            rs.getString("destinations_preferrees"),
                            rs.getDouble("budget")
                    );
                    review = new Review(
                            rs.getInt("id"),
                            rs.getString("comment"),
                            rs.getFloat("rating"),  // Changed from getInt to getFloat
                            user,
                            rs.getInt("hotel_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch review by ID: " + e.getMessage());
        }
        return review;
    }

    public List<Review> getReviewsByHotelId(int hotelId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.* FROM Review r JOIN utilisateur u ON r.user_id = u.id WHERE r.hotel_id = ?";  // Changed 'User' to 'utilisateur'
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getInt("age"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("adresse"),
                            rs.getString("role"),
                            rs.getString("profileImage"),
                            rs.getString("num_telph"),
                            rs.getString("voyageurPreferences"),
                            rs.getString("destinations_preferrees"),
                            rs.getDouble("budget")
                    );
                    Review review = new Review(
                            rs.getInt("id"),
                            rs.getString("comment"),
                            rs.getFloat("rating"),  // Changed from getInt to getFloat
                            user,
                            rs.getInt("hotel_id")
                    );
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch reviews by hotel ID: " + e.getMessage());
        }
        return reviews;
    }
}
