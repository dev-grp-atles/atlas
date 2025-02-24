package tn.esprit.atlas.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeCell;
import javafx.util.Callback;
import tn.esprit.atlas.entities.Post;
import tn.esprit.atlas.entities.Comment;
import tn.esprit.atlas.entities.Category;
import tn.esprit.atlas.services.PostService;
import tn.esprit.atlas.services.CommentService;
import tn.esprit.atlas.services.CategoryService;
import tn.esprit.atlas.PostTreeCell;

import java.util.List;

public class ForumController {

    @FXML
    private TreeView<Post> treeview;

    private PostService postService;
    private CommentService commentService;
    private CategoryService categoryService;

    public ForumController() {
        postService = new PostService();
        commentService = new CommentService();
        categoryService = new CategoryService();
    }

    public void initialize() {
        // Load posts from the database
        List<Post> posts = postService.getAllPosts();

        // Create TreeItems for posts and display them
        TreeItem<Post> root = new TreeItem<>();
        for (Post post : posts) {
            TreeItem<Post> postItem = new TreeItem<>(post);
            root.getChildren().add(postItem);
        }

        // Set the root and items of the TreeView
        treeview.setRoot(root);
        treeview.setShowRoot(false);

        // Set custom cell factory to use PostTreeCell for better UI display
        treeview.setCellFactory(new Callback<TreeView<Post>, TreeCell<Post>>() {
            @Override
            public TreeCell<Post> call(TreeView<Post> postTreeView) {
                return new PostTreeCell();
            }
        });
    }

    /*
    // Add comment to post
    public void addCommentToPost(int postId, String commentText) {
        Post post = postService.getPostById(postId);
        if (post != null) {
            Comment comment = new Comment(commentText);
            post.addComment(comment);
            commentService.save(comment);
            postService.save(post);  // Save the post with updated comments
        }
    }

    // Example for adding a category to a post (if categories exist in DB)
    public void addCategoryToPost(int postId, int categoryId) {
        Post post = postService.getPostById(postId);
        Category category = categoryService.getCategoryById(categoryId);
        if (post != null && category != null) {
            post.addCategory(category);
            postService.save(post);
        }
    }

     */

}
