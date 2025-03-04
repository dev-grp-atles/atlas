package tn.esprit.atlas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import tn.esprit.atlas.controllers.user.AddPostFormController;
import tn.esprit.atlas.controllers.user.EditPostFormController;
import tn.esprit.atlas.entities.Post;
import tn.esprit.atlas.entities.Comment;
import tn.esprit.atlas.services.CommentService;
import tn.esprit.atlas.services.PostService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostTreeCell extends TreeCell<Post> {
    private VBox contentBox;
    private HBox titleBox;
    private Label titleLabel;
    private Button expandButton;
    private Button collapseButton;
    private Button showCommentsButton;
    private Button collapseCommentsButton;
    private Button deletePostButton;  // The "Delete Post" button
    private VBox fullContentBox;
    private TextArea fullContentArea;
    private TextField commentTextField;
    private Button replyButton;

    //private ListView<Label> commentListView = new ListView<>();
    private ListView<HBox> commentListView = new ListView<>();

    private VBox commentBox = new VBox();
    private boolean commentsVisible = false;
    private Button editPostButton;


    private PostService postService = new PostService(); // Service for post operations

    public PostTreeCell() {
        // Initialize elements
        titleBox = new HBox(10);
        titleLabel = new Label();
        expandButton = new Button("Expand");
        collapseButton = new Button("Collapse");
        showCommentsButton = new Button("Show Comments");
        collapseCommentsButton = new Button("Collapse Comments");
        deletePostButton = new Button("Delete Post");
        editPostButton = new Button("Edit Post");

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
        deletePostButton.setOnAction(this::onDeletePostClicked);  // Delete post action
        editPostButton.setOnAction(this::onEditPostClicked);

        // Set layout and add to contentBox
        titleBox.getChildren().addAll(titleLabel, expandButton, collapseButton, showCommentsButton, collapseCommentsButton, deletePostButton,editPostButton);
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
        TreeItem<Post> treeItem = getTreeItem();
        if (treeItem != null) {
            Post post = treeItem.getValue();

            // Fetch the comments using CommentsService
            List<Comment> comments = new CommentService().getAllCommentsByPostId(post.getId());

            // Clear existing items
            commentListView.getItems().clear();

            // Add each comment as an HBox (with a Label, Edit and Delete buttons)
            for (Comment comment : comments) {
                HBox commentHBox = new HBox(10); // Create an HBox with spacing between elements
                Label commentLabel = new Label(comment.getContent());
                Button editButton = new Button("Edit");
                Button deleteButton = new Button("Delete");

                // Action to populate the commentTextField for editing
                editButton.setOnAction(editEvent -> onEditCommentClicked(comment));

                // Action for deleting comment
                deleteButton.setOnAction(deleteEvent -> onDeleteCommentClicked(comment));

                // Add the Label, Edit, and Delete buttons to the HBox
                commentHBox.getChildren().addAll(commentLabel, editButton, deleteButton);

                // Add the HBox to the ListView
                commentListView.getItems().add(commentHBox);
            }

            // Clear the commentBox and add necessary UI components
            commentBox.getChildren().clear();
            commentBox.getChildren().add(commentListView);  // Show ListView of comments
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

    // Flag to check if we're in edit mode
    private boolean isEditingComment = false;

    // The comment object that is being edited
    private Comment editingComment = null;

    private Comment currentCommentToEdit;

    // Handle the edit button click
    private void onEditCommentClicked(Comment comment) {
        // Populate the commentTextField with the content of the comment being edited
        commentTextField.setText(comment.getContent());

        // Change the action of the replyButton to "Update" instead of "Reply"
        replyButton.setText("Update");

        // Add a flag to indicate that we are in edit mode
        isEditingComment = true;
        editingComment = comment;  // Store the comment being edited
    }






    // Method to handle comment deletion
    private void onDeleteCommentClicked(Comment comment) {
        // Create a CommentService instance to handle the database interaction
        CommentService commentService = new CommentService();

        // Delete the comment from the database
        commentService.deleteComment(comment.getId());

        // Remove the corresponding HBox from the ListView
        for (HBox hbox : commentListView.getItems()) {
            // Get the Label from the HBox (assuming the first child is the Label)
            Label label = (Label) hbox.getChildren().get(0);

            // Check if the content matches the comment that should be deleted
            if (label.getText().equals(comment.getContent())) {
                commentListView.getItems().remove(hbox);  // Remove the HBox from the ListView
                break;
            }
        }
    }



    private void onReplyButtonClicked(ActionEvent event) {
        TreeItem<Post> treeItem = getTreeItem();

        if (treeItem != null) {
            Post post = treeItem.getValue();
            String commentContent = commentTextField.getText().trim();

            if (!commentContent.isEmpty()) {
                try {
                    CommentService commentService = new CommentService();

                    // Check if we're in edit mode
                    if (isEditingComment) {
                        if (editingComment == null) {
                            // If for some reason editingComment is null, print a log message and exit
                            System.out.println("Error: No comment is being edited.");
                            return;
                        }

                        // Set the content of the comment to the new content from the text field
                        editingComment.setContent(commentContent);

                        // Update the comment in the database
                        commentService.updateComment(editingComment);

                        // After updating, clear the text field and reset the button
                        commentTextField.clear();
                        replyButton.setText("Reply");  // Reset the button to "Reply"
                        isEditingComment = false;  // Reset the edit flag
                        editingComment = null;  // Clear the comment being edited
                    } else {
                        // Add a new comment
                        Comment comment = new Comment();
                        comment.setContent(commentContent);
                        comment.setPostId(post.getId());  // Assuming the Post has an ID

                        // Add the comment to the database
                        commentService.addComment(comment);

                        // Clear the comment text field after adding the comment
                        commentTextField.clear();
                    }

                    // Fetch updated list of comments from the CommentService
                    List<Comment> updatedComments = commentService.getAllCommentsByPostId(post.getId());

                    // Clear the existing items in the commentListView
                    commentListView.getItems().clear();

                    // Loop through the updated list of comments
                    for (Comment c : updatedComments) {
                        // Create an HBox to represent the comment
                        HBox commentHBox = new HBox();
                        commentHBox.setSpacing(10); // Space between elements in HBox

                        // Create a Label for the comment content
                        Label commentLabel = new Label(c.getContent());

                        // Optionally add buttons or other components (e.g., Edit, Delete buttons)
                        Button editButton = new Button("Edit");
                        editButton.setOnAction(e -> onEditCommentClicked(c)); // Pass the comment to the edit handler

                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(e -> onDeleteCommentClicked(c)); // Pass the comment to the delete handler

                        // Add the Label and Buttons to the HBox
                        commentHBox.getChildren().addAll(commentLabel, editButton, deleteButton);

                        // Add the HBox to the commentListView
                        commentListView.getItems().add(commentHBox);
                    }
                } catch (SQLException e) {
                    System.out.println("Error occurred while adding/updating comment: " + e.getMessage());
                    e.printStackTrace();  // Log the stack trace for debugging purposes
                }
            }
        }
    }






    // Action for collapsing comments
    private void onCollapseCommentsClicked(ActionEvent event) {
        commentBox.getChildren().clear(); // Hide the comment section
        commentsVisible = false; // Track the collapse state (optional, to manage further interactions)
    }













    private void onDeletePostClicked(ActionEvent event) {
        TreeItem<Post> treeItem = getTreeItem();
        if (treeItem != null) {
            Post post = treeItem.getValue();

            // Show confirmation dialog before deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Post");
            alert.setHeaderText("Are you sure you want to delete this post?");
            alert.setContentText(post.getTitle());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete all comments associated with this post
                    List<Comment> comments = new CommentService().getAllCommentsByPostId(post.getId());
                    for (Comment comment : comments) {
                        new CommentService().deleteComment(comment.getId()); // Delete each comment
                    }

                    // Delete the post from the database
                    postService.deletePost(post.getId()); // Assuming postService.deletePost() method exists

                    // Remove the post from the TreeView
                    getTreeItem().getParent().getChildren().remove(getTreeItem());
                    System.out.println("Post and its comments deleted successfully.");
                }
            });
        }
    }


    private void onEditPostClicked(ActionEvent event) {
        try {
            // Load the EditPostForm FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/community/EditPostForm.fxml"));
            Parent editPostRoot = loader.load();

            // Get the post from the TreeItem
            TreeItem<Post> treeItem = getTreeItem();
            Post post = treeItem.getValue();

            // Get the controller of the EditPostForm
            EditPostFormController controller = loader.getController();

            // Pre-populate the form with the current post's details
            controller.setPostDetails(post);

            // Instead of creating a new scene, set the root of the current scene
            Scene currentScene = ((Node) event.getSource()).getScene();
            Stage currentStage = (Stage) currentScene.getWindow();

            // Set the new root to the EditPostForm without changing the entire scene.
            // In this way, the UI remains the same, but the content changes.
            currentStage.getScene().setRoot(editPostRoot);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading edit post form: " + e.getMessage());
        }
    }







}
