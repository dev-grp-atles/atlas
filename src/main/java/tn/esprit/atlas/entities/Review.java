package tn.esprit.atlas.entities;

public class Review {
    private int id;
    private String reviewerName;
    private String comment;
    private int rating;
    private int hotelId;

    // Default constructor
    public Review() {
    }

    // Parameterized constructor
    public Review(int id, String reviewerName, String comment, int rating, int hotelId) {
        this.id = id;
        this.reviewerName = reviewerName;
        this.comment = comment;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

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