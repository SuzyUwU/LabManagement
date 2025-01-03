package org.example.View;

import org.example.Models.Lab;
import org.example.Models.Activity;
import org.example.Models.User;
import org.example.View.SideMenu.ActivitiesGUI;
import org.example.View.SideMenu.AddActivityGUI;
import org.example.View.SideMenu.JoinActivityGUI;
import org.example.View.SideMenu.LabsGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainMenuGUI {
    private final User currentUser;
    private final JFrame frame;

    public MainMenuGUI(User user) {
        this.currentUser = user; // Initialize currentUser
        currentUser.getUserId();
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 1, 10, 10));

        JLabel title = new JLabel("Main Menu", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title);

        JButton addActivityButton = new JButton("Add/Make Lab Activity");
        JButton joinActivityButton = new JButton("Join Lab Activity");
        JButton showLabsButton = new JButton("Show Labs");
        JButton showLabActivitiesButton = new JButton("Show Lab Activities");
        JButton returnToLoginButton = new JButton("Return to Login Menu");

        // Add listeners to buttons
        addActivityButton.addActionListener(e -> showAddActivityDialog());
        joinActivityButton.addActionListener(e -> showJoinActivityDialog());
        showLabsButton.addActionListener(e -> showLabsDialog());
        showLabActivitiesButton.addActionListener(e -> showAllActivitiesDialog());
        returnToLoginButton.addActionListener(e -> returnToLoginMenu());

        frame.add(addActivityButton);
        frame.add(joinActivityButton);
        frame.add(showLabsButton);
        frame.add(showLabActivitiesButton);
        frame.add(returnToLoginButton);

        frame.setVisible(true);
    }

    private void showAddActivityDialog() {

        AddActivityGUI.showDialog();
    }

    private void showJoinActivityDialog() {
        JoinActivityGUI.showDialog(frame);
    }

    private void showLabsDialog() {
        Lab labModel = new Lab();

        Activity activityManager = new Activity();
        List<Activity> activities = activityManager.getAvailableActivity();

        LabsGUI.showLabs(labModel, activities);
    }

    private void showAllActivitiesDialog() {
        Activity activityManager = new Activity();
        List<Activity> activities = activityManager.getAvailableActivity();
        ActivitiesGUI.showActivities(activities, currentUser);
    }

    private void returnToLoginMenu() {
        frame.dispose(); // Close the Main Menu
        LoginMenuGUI.ShowGUI();
    }

}
