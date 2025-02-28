package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.Category;
import tn.esprit.atlas.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private static Connection connection;

    public CategoryService() {
        this.connection = DatabaseConnection.getInstance().getCnx();
    }

    // ➤ Add Category
    public void addCategory(Category category) throws SQLIntegrityConstraintViolationException {
        String query = "INSERT INTO category (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getName());

            preparedStatement.executeUpdate();
            System.out.println("✅ Category added successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            // Re-throw the exception to handle it in the controller
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add category to the database.", e);
        }
    }

    // ➤ Get All Categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // ➤ Update Category
    public void updateCategory(Category category) {
        String query = "UPDATE category SET name = ? WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());

            preparedStatement.executeUpdate();
            System.out.println("✅ Category updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Delete Category
    public void deleteCategory(int categoryId) {
        String query = "DELETE FROM category WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
            System.out.println("✅ Category deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Find Category by ID
    public Category findCategoryById(int categoryId) {
        String query = "SELECT * FROM category WHERE category_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
