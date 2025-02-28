package tn.esprit.atlas;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import tn.esprit.atlas.entities.Post;
import tn.esprit.atlas.entities.Comment;
import tn.esprit.atlas.services.CommentService;
import tn.esprit.atlas.services.PostService;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

public class PostTreeCell extends TreeCell<Post> {
    private VBox contentBox;
    private HBox titleBox;
    private Label titleLabel;
    private Button expandButton;
    private Button collapseButton;
    private Button showCommentsButton;
    private Button collapseCommentsButton;
    private VBox fullContentBox;
    private TextArea fullContentArea;
    private TextField commentTextField;
    private Button replyButton;
    private ListView<Label> commentListView = new ListView<>();
    private VBox commentBox = new VBox();
    private boolean commentsVisible = false;



    public PostTreeCell() {
        // Initialize elements
        titleBox = new HBox(10);
        titleLabel = new Label();
        expandButton = new Button("Expand");
        collapseButton = new Button("Collapse");
        showCommentsButton = new Button("Show Comments");
        collapseCommentsButton = new Button("Collapse Comments");


        contentBox = new VBox(5);
        fullContentBox = new VBox(10);

        commentTextField = new TextField();
        replyButton = new Button("Reply");



        // Button actions
        expandButton.setOnAction(this::onExpandButtonClicked);
        collapseButton.setOnAction(this::onCollapseButtonClicked);
        showCommentsButton.setOnAction(this::onShowCommentsClicked);
        collapseCommentsButton.setOnAction(this::onCollapseCommentsClicked);
        replyButton.setOnAction(this::onReplyButtonClicked);


        // Set layout and add to contentBox

        titleBox.getChildren().addAll(titleLabel, expandButton, collapseButton, showCommentsButton, collapseCommentsButton);


        contentBox.getChildren().addAll(titleBox);






        /*
        contentBox.getChildren().add(newPostForm);  // Place the new post form at the top
        setGraphic(contentBox);
         */
        contentBox.getChildren().clear();
        // Place the new post form at the top
        contentBox.getChildren().add(titleBox);

        setGraphic(contentBox);
    }

    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);
        if (empty || post == null) {
            setText(null);
            setGraphic(null);
        } else {
            titleLabel.setText(post.getTitle());

            // Initially show the preview (or full content based on expansion)
            contentBox.getChildren().clear();
            contentBox.getChildren().addAll(titleBox);
            setGraphic(contentBox);
        }
    }

    private void onExpandButtonClicked(ActionEvent event) {
        // Expand the content and show full content in a scrollable area
        TreeItem<Post> treeItem = getTreeItem();
        if (treeItem != null) {
            Post post = treeItem.getValue();
            contentBox.getChildren().clear();
            fullContentArea = new TextArea(post.getFullContent());
            fullContentArea.setWrapText(true); // Enable text wrapping
            fullContentArea.setEditable(false); // Disable text editing
            contentBox.getChildren().addAll(titleBox, fullContentArea);
        }
    }

    private void onCollapseButtonClicked(ActionEvent event) {
        // Collapse full content back to preview (no preview field, just title for now)
        contentBox.getChildren().clear();
        contentBox.getChildren().addAll(titleBox); // Only show title
        setGraphic(contentBox);
    }

    // Action for showing comments
    private void onShowCommentsClicked(ActionEvent event) {
        System.out.println("showCommentsClicked");
        TreeItem<Post> treeItem = getTreeItem();
        if (treeItem != null) {
            Post post = treeItem.getValue();

            // Fetch the comments using CommentsService
            List<Comment> comments = new CommentService().getAllCommentsByPostId(post.getId());

            // Clear existing items
            commentListView.getItems().clear();

            // Add each comment as a Label
            for (Comment comment : comments) {
                commentListView.getItems().add(new Label(comment.getContent()));  // Display each comment as a label
            }

            // Clear the commentBox and add necessary UI components
            commentBox.getChildren().clear();
            commentBox.getChildren().add(commentListView);  // Show ListView of comments

            // Optional: Add a text field and a reply button for adding new comments (if needed)
            commentBox.getChildren().add(commentTextField);  // Add comment text field
            commentBox.getChildren().add(replyButton);       // Add reply button

            // Add commentBox back to the contentBox if it's not already there
            if (!contentBox.getChildren().contains(commentBox)) {
                contentBox.getChildren().add(commentBox); // Add comments section to the post
            }

            // Set the comments section visible
            commentsVisible = true;
        }
    }

    // Action for collapsing comments
    private void onCollapseCommentsClicked(ActionEvent event) {
        commentBox.getChildren().clear(); // Hide the comment section
        commentsVisible = false; // Track the collapse state (optional, to manage further interactions)
    }

    private void onReplyButtonClicked(ActionEvent event) {
        System.out.println("onReplyButtonClicked");
        TreeItem<Post> treeItem = getTreeItem();

        System.out.println("TreeItem: " + getTreeItem());

        if (treeItem != null) {
            Post post = treeItem.getValue();
            String commentContent = commentTextField.getText().trim();


            System.out.println(commentContent);


            if (!commentContent.isEmpty()) {
                // Assuming you have a CommentService that can add the comment to the database
                CommentService commentService = new CommentService();

                try {
                    // Create a new Comment entity
                    Comment comment = new Comment();
                    comment.setContent(commentContent);
                    comment.setPostId(post.getId());  // Assuming the Post has an ID

                    // Add the comment to the database
                    commentService.addComment(comment);
                    System.out.println("Comment added to the database!");

                    // Clear the comment text field
                    commentTextField.clear();

                    // Fetch updated list of comments from the CommentService
                    List<Comment> updatedComments = commentService.getAllCommentsByPostId(post.getId());

                    // Update the comment list view with the newly added comment
                    commentListView.getItems().clear();
                    for (Comment c : updatedComments) {
                        commentListView.getItems().add(new Label(c.getContent())); // Add each comment as a label
                    }

                    // Re-add the commentBox with the updated comment list and the text field
                    commentBox.getChildren().clear();
                    commentBox.getChildren().add(commentListView);  // Re-add ListView
                    commentBox.getChildren().add(commentTextField);  // Re-add text field
                    commentBox.getChildren().add(replyButton);       // Re-add reply button

                } catch (SQLException e) {
                    System.out.println("Error occurred while adding comment: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Comment cannot be empty");
            }
        }
    }



}
