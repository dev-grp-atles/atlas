package tn.esprit.atlas.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.Post;
import tn.esprit.atlas.services.PostService;
import java.sql.*;

import java.io.IOException;

public class EditPostFormController {

    @FXML
    private TextArea contentTextArea;  // This will be injected by FXML

    private Post postToEdit;

    @FXML
    private void initialize() {
        // You can add debug print statements to ensure that contentTextArea is injected correctly
        System.out.println("FXML is loaded, contentTextArea = " + contentTextArea);
    }

    // This method will be called to set the post details
    public void setPostDetails(Post post) {
        this.postToEdit = post;
        if (post != null && contentTextArea != null) {
            contentTextArea.setText(post.getFullContent());  // Pre-fill contentTextArea with the current post's content
        }
    }

    @FXML
    private void onSaveChangesClicked(ActionEvent event) {
        // Get the updated content from the TextArea
        String updatedContent = contentTextArea.getText().trim();

        if (postToEdit != null && !updatedContent.isEmpty()) {
            postToEdit.setFullContent(updatedContent);  // Update the content of the post

            // Set the updated_at timestamp to the current time
            postToEdit.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            // Call the PostService to update the post in the database
            PostService postService = new PostService();
            postService.updatePost(postToEdit);

            // After saving, you will load the forum view to navigate back to the forum
            try {
                // Load the forum FXML file (you could load just the forum's list part)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/community/forum_view.fxml"));
                Parent forumRoot = loader.load();

                // Get the controller for the forum (if needed)
                // ForumController forumController = loader.getController(); // If needed

                // Get the current stage and set the new scene (forum view)
                Stage currentStage = (Stage) contentTextArea.getScene().getWindow();
                currentStage.getScene().setRoot(forumRoot);  // Set the forum scene as the root

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading Forum view after saving changes: " + e.getMessage());
            }
        }
    }


}
