package webshop.BAL;

import webshop.DAL.UserDAO;
import webshop.DAL.Model.User;

import java.sql.SQLException;

public class UserService {
    
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    // Register a new user
    public User registerUser(String username, String password, String email) throws SQLException {
        // Check if username already exists
        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Create new user
        User newUser = new User();
        newUser.setUsername(username);
        // In a real application, you would hash the password
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRole("USER"); // Default role for new users
        
        return userDAO.createUser(newUser);
    }
    
    // Authenticate user - updated to use the new method
    public User login(String username, String password) throws SQLException {
        // Use the direct method that checks both username and password
        return userDAO.getUserByUsernameAndPassword(username, password);
    }
    
    // Get user by ID
    public User getUserById(int id) throws SQLException {
        return userDAO.getUserById(id);
    }
}