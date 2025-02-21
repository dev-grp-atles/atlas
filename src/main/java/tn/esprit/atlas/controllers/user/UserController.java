package tn.esprit.atlas.controllers.user;

import tn.esprit.atlas.entities.User;
import tn.esprit.atlas.services.UserService;
import tn.esprit.atlas.utils.UserSession;

import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserController {

    private static UserService userService = new UserService();

    // ➤ Create User
    public void createUser(User user) throws SQLIntegrityConstraintViolationException {
        userService.addUser(user);
    }

    // ➤ List All Users and Return the List
    public List<User> listUsers() {
        return UserService.getAllUsers(); // Return the list of users
    }

    // ➤ Modify User
    public void modifyUser(User user) {
        userService.updateUser(user);
    }

    // ➤ Remove User
    public static void removeUser(int userId) {
        userService.deleteUser(userId);
    }

    // ➤ Find User by Email or ID
    public void findUser(String email, int id) {
        userService.findUser(email, id);
    }

    // ➤ Sign In User and Return the User Object
    public User signInUser(String email, String password) {
        return userService.signInUser(email, password); // Returns the user if credentials are correct, null otherwise
    }

    // ➤ Logout User and Clear Session
    public static void logout() {
        // Clear the current user session
        UserSession.clearSession();
        System.out.println("User logged out successfully!");
    }
}