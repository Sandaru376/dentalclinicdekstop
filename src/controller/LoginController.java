package controller;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    /** Returns the logged-in User, or null if the credentials are wrong. */
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;
        if (!PasswordUtil.matches(password, user.getPassword())) return null;
        return user;
    }
}
