package org.example.View.SideMenu;

import javax.swing.*;
import java.awt.*;

import org.bson.Document;
import org.example.Models.User;
import org.example.View.MainMenuGUI;

public class LoginandRegister {
    private static final User user = new User();

    public static void showLoginDialog(JFrame frame) {
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField idOrUsernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("ID or Username:"));
        loginPanel.add(idOrUsernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(frame, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String idOrUsername = idOrUsernameField.getText();
            String password = new String(passwordField.getPassword());

            long userId = 0;
            String username = null;
            try {
                userId = Long.parseLong(idOrUsername); // Try to parse as userId
            } catch (NumberFormatException e) {
                username = idOrUsername; // Treat input as username if parsing fails
            }

            // Use validateCredentials to check login
            if (user.validateCredentials(userId, username, password)) {
                Document userDocument = user.getUser(userId, username); // Ensures currentUserId is updated
                if (userDocument != null) {
                    String displayName = userDocument.getString("username");
                    JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + displayName);
                    frame.dispose();
                    MainMenuGUI mainMenu = new MainMenuGUI(user);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error retrieving user details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }



    public static void showRegisterDialog(JFrame frame) {
        JPanel registerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        registerPanel.add(new JLabel("New Username:"));
        registerPanel.add(usernameField);
        registerPanel.add(new JLabel("New Password:"));
        registerPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(frame, registerPanel, "Register", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                user.addUser(username, password); // Use `addUser` method from `User` class
                JOptionPane.showMessageDialog(frame, "Registration successful! You can now log in.");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
