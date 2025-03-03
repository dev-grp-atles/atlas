package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Review;
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
        String query = "INSERT INTO Review (reviewer_name, comment, rating, hotel_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, review.getReviewerName());
            stmt.setString(2, review.getComment());
            stmt.setInt(3, review.getRating());
            stmt.setInt(4, review.getHotelId());
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
        String query = "UPDATE Review SET reviewer_name = ?, comment = ?, rating = ?, hotel_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, review.getReviewerName());
            stmt.setString(2, review.getComment());
            stmt.setInt(3, review.getRating());
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
        String query = "SELECT * FROM Review";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("id"),
                        rs.getString("reviewer_name"),
                        rs.getString("comment"),
                        rs.getInt("rating"),
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
        // This method is required by the IService interface but is not implemented here.
        // You can implement it based on your requirements.
        throw new UnsupportedOperationException("getOne() method is not implemented.");
    }

    /**
     * Get a review by its ID.
     *
     * @param id The ID of the review.
     * @return The review object, or null if not found.
     */
    public Review getById(int id) {
        Review review = null;
        String query = "SELECT * FROM Review WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    review = new Review(
                            rs.getInt("id"),
                            rs.getString("reviewer_name"),
                            rs.getString("comment"),
                            rs.getInt("rating"),
                            rs.getInt("hotel_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch review by ID: " + e.getMessage());
        }
        return review;
    }

    /**
     * Get all reviews for a specific hotel.
     *
     * @param hotelId The ID of the hotel.
     * @return A list of reviews for the hotel.
     */
    public List<Review> getReviewsByHotelId(int hotelId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Review WHERE hotel_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, hotelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review(
                            rs.getInt("id"),
                            rs.getString("reviewer_name"),
                            rs.getString("comment"),
                            rs.getInt("rating"),
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