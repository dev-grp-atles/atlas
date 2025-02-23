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
        String query = "INSERT INTO Review (comment, rating, hotel_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, review.getComment());
            stmt.setInt(2, review.getRating());
            stmt.setInt(3, review.getHotelId());
            stmt.executeUpdate();
            System.out.println("Review added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Review review) {
        String query = "UPDATE Review SET comment = ?, rating = ?, hotel_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, review.getComment());
            stmt.setInt(2, review.getRating());
            stmt.setInt(3, review.getHotelId());
            stmt.setInt(4, review.getId());
            stmt.executeUpdate();
            System.out.println("Review updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Review review) {
        String query = "DELETE FROM Review WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, review.getId());
            stmt.executeUpdate();
            System.out.println("Review deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
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
                        rs.getString("comment"),
                        rs.getInt("rating"),
                        rs.getInt("hotel_id")
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public Review getOne() {
        // Example to get a specific review by ID (you can extend this based on your needs)
        return null;
    }


    // New method to get a review by ID
    public Review getById(int id) {
        Review review = null;
        String query = "SELECT * FROM Review WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    review = new Review(
                            rs.getInt("id"),
                            rs.getString("comment"),
                            rs.getInt("rating"),
                            rs.getInt("hotel_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }
}

