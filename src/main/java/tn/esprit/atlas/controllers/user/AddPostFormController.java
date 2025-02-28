package tn.esprit.atlas.controllers.user;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tn.esprit.atlas.entities.Category;
import tn.esprit.atlas.entities.Post;
import tn.esprit.atlas.services.CategoryService;
import tn.esprit.atlas.services.PostService;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

public class AddPostFormController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private TextField category_Field;

    @FXML
    private void handleSubmitAction(ActionEvent event) throws SQLIntegrityConstraintViolationException {
        String title = titleField.getText();
        String content = contentArea.getText();
        String categoryName = category_Field.getText();  // Assuming category_Field is used for the category input

        // Ensure the fields are not empty
        if (!title.isEmpty() && !content.isEmpty() && !categoryName.isEmpty()) {
            // Create a new Post object
            Post newPost = new Post();
            newPost.setTitle(title);
            newPost.setFullContent(content);

            // Set created and updated timestamps (current time)
            newPost.setCreatedAt(new Date());
            newPost.setUpdatedAt(new Date());

            // Set view count to 0 initially
            newPost.setViewCount(0);

            // Create an instance of CategoryService
            CategoryService categoryService = new CategoryService();

            // Get the category ID based on the category name
            int categoryId = getCategoryIdByName(categoryName, categoryService);

            if (categoryId == -1) {
                System.out.println("Category not found. Would you like to add it?");

                // Optionally, prompt the user to add the category
                if (askUserToAddCategory()) {
                    // Add the new category
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    categoryService.addCategory(newCategory);

                    // Get the category ID again after adding it
                    categoryId = newCategory.getId();
                    System.out.println("✅ New Category Added: " + categoryName);
                } else {
                    // Exit method if category is not added
                    return;
                }
            }

            // Set the valid categoryId
            newPost.setCategoryId(categoryId);

            // Create an instance of PostService
            PostService postService = new PostService();

            // Add the new post to the database
            postService.addPost(newPost);

            // Clear the form after submission
            clearForm();

            System.out.println("✅ New Post Created: " + title);
        } else {
            System.out.println("Please fill out all fields: title, content, and category.");
        }
    }

    // This method checks if the category exists in the database and returns the category_id
    private int getCategoryIdByName(String categoryName, CategoryService categoryService) {
        // Find category by name using the CategoryService
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category.getId();  // Return the category id if found
            }
        }
        return -1;  // Return -1 if the category does not exist
    }

    // This method will prompt the user to decide if they want to add the category
    private boolean askUserToAddCategory() {
        // You can show a simple dialog here to ask the user if they want to add the category
        // For now, we'll assume the user agrees to add it (you can improve this part)
        return true;  // This is where you'd ask the user for confirmation
    }

    // Clears the form fields after submission
    private void clearForm() {
        titleField.clear();
        contentArea.clear();
        category_Field.clear();
    }



    // Handles "Go to Forum" button click
    @FXML
    private void handleGoToForumAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/atlas/views/community/forum_view.fxml"));
            Parent forumRoot = loader.load();
            Stage currentStage = (Stage) titleField.getScene().getWindow();
            currentStage.setScene(new Scene(forumRoot));  // Set new scene on the same stage
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading forum view: " + e.getMessage());
        }
    }


}
