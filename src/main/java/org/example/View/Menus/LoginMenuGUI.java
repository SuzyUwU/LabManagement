package org.example.View.Menus;

import javax.swing.*;
import java.awt.*;

import static org.example.View.SideMenu.LoginAndRegister.showLoginDialog;
import static org.example.View.SideMenu.LoginAndRegister.showRegisterDialog;

public class LoginMenuGUI {

    public static void ShowGUI() {
        JFrame frame = new JFrame("Login Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        panel.add(title, gbc);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        gbc.gridy = 1;
        panel.add(loginButton, gbc);

        gbc.gridy++;
        panel.add(registerButton, gbc);

        gbc.gridy++;
        panel.add(exitButton, gbc);

        frame.setVisible(true);

        loginButton.addActionListener(e -> showLoginDialog(frame));
        registerButton.addActionListener(e -> showRegisterDialog(frame));
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Exiting the app...");
            System.exit(0);
        });
    }
}
