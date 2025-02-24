package tn.esprit.atlas.entities;

import java.util.Date;

public class Post {

    private int id;
    private String title;
    private String fullContent;
    private Date createdAt;
    private Date updatedAt;
    private int viewCount;   // New attribute for view count
    private int categoryId;  // New attribute for category ID

    // Getters and setters for the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullContent() {
        return fullContent;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getViewCount() {   // Getter for viewCount
        return viewCount;
    }

    public void setViewCount(int viewCount) {   // Setter for viewCount
        this.viewCount = viewCount;
    }

    public int getCategoryId() {   // Getter for categoryId
        return categoryId;
    }

    public void setCategoryId(int categoryId) {   // Setter for categoryId
        this.categoryId = categoryId;
    }
}
