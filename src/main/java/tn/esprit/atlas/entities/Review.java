package tn.esprit.atlas.entities;

public class Review {
    private int id;
    private String reviewerName;
    private String comment;
    private float rating;
    private int hotelId;
    private User user; // Reference to the User entity

    // Default constructor
    public Review() {
    }

    // Parameterized constructor
    public Review(int id, String comment, float rating, User user, int hotelId) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.user = user;  // Set the user object directly
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

    public User getUser() { return user; } // Getter for User
    public void setUser(User user) { this.user = user; } // Setter for User



    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }


    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewerName='" + reviewerName + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", hotelId=" + hotelId +
                '}';
    }
}