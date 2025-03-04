package tn.esprit.atlas.utils;

import tn.esprit.atlas.entities.User;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void setUser(User user) {
        instance = new UserSession(user);
    }

    public static User getUser() {
        return instance != null ? instance.user : null;
    }

    public static void clearSession() {
        instance = null;
    }
}
