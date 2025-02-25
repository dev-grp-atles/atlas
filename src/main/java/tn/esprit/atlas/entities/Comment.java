package tn.esprit.atlas.entities;

import java.util.Date;

public class Comment {
    private int id;
    private String content;
    private Date createdAt;
    private int postId; // Assuming this is used to link the comment to a post

    // Constructors
    public Comment() {}

    public Comment(int id, String content, Date createdAt, int postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.postId = postId;
    }

    public Comment(String commentText) {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", postId=" + postId +
                '}';
    }
}
