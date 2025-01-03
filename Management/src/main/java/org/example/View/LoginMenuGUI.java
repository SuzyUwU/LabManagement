package org.example.View;

import org.example.Models.Activity;

import javax.swing.*;
import java.awt.*;

import static org.example.View.SideMenu.LoginandRegister.showLoginDialog;
import static org.example.View.SideMenu.LoginandRegister.showRegisterDialog;

public class LoginMenuGUI {

    public static void ShowGUI() {
        // Create the frame
        JFrame frame = new JFrame("Login Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create a panel with GridBagLayout for center alignment
        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        // Create constraints for button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons
        gbc.gridx = 0; // Column position
        gbc.gridy = 0; // Row position
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch buttons horizontally

        // Add title label
        JLabel title = new JLabel("Login Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0; // Set at the top
        panel.add(title, gbc);

        // Create buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        // Add buttons to the panel
        gbc.gridy = 1; // Increment row position
        panel.add(loginButton, gbc);

        gbc.gridy++; // Move to next row
        panel.add(registerButton, gbc);

        gbc.gridy++; // Move to next row
        panel.add(exitButton, gbc);

        // Set frame visibility
        frame.setVisible(true);

        // Add button functionality
        loginButton.addActionListener(e -> showLoginDialog(frame));
        registerButton.addActionListener(e -> showRegisterDialog(frame));
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Exiting the app...");
            System.exit(0);
        });
    }
}
