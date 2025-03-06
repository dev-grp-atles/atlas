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
        // Validate review and user_id
        if (review == null || review.getUserId() <= 0) {
            System.err.println("Invalid review or user_id: " + (review == null ? "null" : review.getUserId()));
            return;
        }

        // Debugging output
        System.out.println("Review object details:");
        System.out.println("Review ID: " + review.getId());
        System.out.println("User ID: " + review.getUserId());
        System.out.println("Comment: " + review.getComment());
        System.out.println("Rating: " + review.getRating());
        System.out.println("Hotel ID: " + review.getHotelId());

        // Verify the user_id exists in the utilisateur table
        String checkUserQuery = "SELECT id FROM utilisateur WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery)) {
            checkStmt.setInt(1, review.getUserId());
            try (var rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("User ID " + review.getUserId() + " does not exist in the utilisateur table.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to verify user_id: " + e.getMessage());
            return;
        }

        // Update the review
        String updateQuery = "UPDATE Review SET comment = ?, rating = ?, user_id = ?, hotel_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, review.getComment());
            stmt.setFloat(2, review.getRating());
            stmt.setInt(3, review.getUserId()); // Use the userId field
            stmt.setInt(4, review.getHotelId());
            stmt.setInt(5, review.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Review updated successfully in the database!");
            } else {
                System.out.println("No review found with ID: " + review.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to update review in the database: " + e.getMessage());
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
        String query = "SELECT r.*, u.* FROM Review r JOIN utilisateur u ON r.user_id = u.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("u.id"), // Use alias to avoid ambiguity
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
                        rs.getInt("r.id"), // Use alias to avoid ambiguity
                        rs.getString("comment"),
                        rs.getFloat("rating"),
                        rs.getInt("user_id"), // Use the user_id field
                        rs.getInt("hotel_id")
                );
                review.setUser(user); // Set the User object
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
        String query = "SELECT r.*, u.* FROM Review r JOIN utilisateur u ON r.user_id = u.id WHERE r.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("u.id"), // Use alias to avoid ambiguity
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
                            rs.getInt("r.id"), // Use alias to avoid ambiguity
                            rs.getString("comment"),
                            rs.getFloat("rating"),
                            rs.getInt("user_id"), // Use the user_id field
                            rs.getInt("hotel_id")
                    );
                    review.setUser(user); // Set the User object
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch review by ID: " + e.getMessage());
        }
        return review;
    }

    public List<Review> getReviewsByHotelId(int hotelId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.* FROM Review r JOIN utilisateur u ON r.user_id = u.id WHERE r.hotel_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("u.id"), // Use alias to avoid ambiguity
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
                            rs.getInt("r.id"), // Use alias to avoid ambiguity
                            rs.getString("comment"),
                            rs.getFloat("rating"),
                            rs.getInt("user_id"), // Use the user_id field
                            rs.getInt("hotel_id")
                    );
                    review.setUser(user); // Set the User object
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch reviews by hotel ID: " + e.getMessage());
        }
        return reviews;
    }
}