package tn.esprit.atlas.entities;

public class Review {
    private int id;
    private String reviewerName;
    private String comment;
    private float rating;
    private int hotelId;
    private int userId; // Add this field to store the user ID
    private User user; // Reference to the User entity (optional)

    // Default constructor
    public Review() {
    }

    // Parameterized constructor
    public Review(int id, String comment, float rating, int userId, int hotelId) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.userId = userId; // Set the user ID directly
        this.hotelId = hotelId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId(); // Sync the user ID with the User object
        }
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewerName='" + reviewerName + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", hotelId=" + hotelId +
                ", userId=" + userId +
                ", user=" + (user != null ? user.getId() : "null") + // Include user ID in toString for debugging
                '}';
    }
}