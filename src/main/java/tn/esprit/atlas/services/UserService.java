package tn.esprit.atlas.services;

import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private static Connection connection;

    public UserService() {
        this.connection = DatabaseConnection.getInstance().getCnx();
    }

    // ➤ Add User
    public void addUser(User user) throws SQLIntegrityConstraintViolationException {
        String query = "INSERT INTO Utilisateur (name, surname, age, email, password, adresse, role, profileImage, num_telph, voyageurPreferences, destinations_preferrees, budget) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, hashedPassword);
            preparedStatement.setString(6, user.getAdresse());
            preparedStatement.setString(7, user.getRole());
            preparedStatement.setString(8, user.getProfileImage());
            preparedStatement.setString(9, user.getNumTel());
            preparedStatement.setString(10, user.getVoyageurPreferences());
            preparedStatement.setString(11, user.getDestinationsPreferrees());
            preparedStatement.setDouble(12, user.getBudget());

            preparedStatement.executeUpdate();
            System.out.println("✅ User added successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            // Re-throw the exception to handle it in the controller
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add user to the database.", e);
        }
    }

    // ➤ Get All Users
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("adresse"),
                        resultSet.getString("role"),
                        resultSet.getString("profileImage"),
                        resultSet.getString("num_telph"),
                        resultSet.getString("voyageurPreferences"),
                        resultSet.getString("destinations_preferrees"),
                        resultSet.getDouble("budget")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // ➤ Update User
    public void updateUser(User user) {
        String query = "UPDATE Utilisateur SET name = ?, surname = ?, age = ?, email = ?, password = ?, adresse = ?, role = ?, profileImage = ?, num_telph = ?, voyageurPreferences = ?, destinations_preferrees = ?, budget = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getAdresse());
            preparedStatement.setString(7, user.getRole());
            preparedStatement.setString(8, user.getProfileImage());
            preparedStatement.setString(9, user.getNumTel());
            preparedStatement.setString(10, user.getVoyageurPreferences());
            preparedStatement.setString(11, user.getDestinationsPreferrees());
            preparedStatement.setDouble(12, user.getBudget());
            preparedStatement.setInt(13, user.getId());

            preparedStatement.executeUpdate();
            System.out.println("✅ User updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Delete User
    public void deleteUser(int userId) {
        String query = "DELETE FROM Utilisateur WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            System.out.println("✅ User deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➤ Find User by Email or ID
    public User findUser(String email, int id) {
        String query = "SELECT * FROM Utilisateur WHERE email = ? OR id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("adresse"),
                        resultSet.getString("role"),
                        resultSet.getString("profileImage"),
                        resultSet.getString("num_telph"),
                        resultSet.getString("voyageurPreferences"),
                        resultSet.getString("destinations_preferrees"),
                        resultSet.getDouble("budget")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ➤ Sign In User
    public User signInUser(String email, String password) {
        String query = "SELECT * FROM Utilisateur WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getInt("age"),
                            resultSet.getString("email"),
                            hashedPassword,
                            resultSet.getString("adresse"),
                            resultSet.getString("role"),
                            resultSet.getString("profileImage"),
                            resultSet.getString("num_telph"),
                            resultSet.getString("voyageurPreferences"),
                            resultSet.getString("destinations_preferrees"),
                            resultSet.getDouble("budget")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}