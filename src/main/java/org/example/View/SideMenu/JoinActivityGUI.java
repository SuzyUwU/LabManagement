package org.example.View.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Models.Activity;

public class JoinActivityGUI {
    public static void showDialog(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField userIdField = new JTextField();
        JComboBox<String> activityDropdown = new JComboBox<>();

        // Fetch available activities
        Activity activityManager = new Activity();
        List<Activity> availableActivities = activityManager.getAvailableActivity();
        for (Activity activity : availableActivities) {
            activityDropdown.addItem(
                    "Lab ID: " + activity.getLabId() +
                            " | Activity ID: " + activity.getActivityId() +
                            " | " + activity.getActivityName() +
                            " | Time Slot: " + activity.getActiveTimeSlots()
            );
        }

        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Select Activity:"));
        panel.add(activityDropdown);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Join Activity", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate and parse the User ID
                String userIdText = userIdField.getText().trim();
                if (userIdText.isEmpty()) {
                    throw new Exception("User ID field cannot be empty.");
                }

                long userId = Long.parseLong(userIdText);
                if (userId <= 0) {
                    throw new Exception("User ID must be a positive number.");
                }

                String selectedActivity = (String) activityDropdown.getSelectedItem();
                if (selectedActivity == null || selectedActivity.isEmpty()) {
                    throw new Exception("Please select an activity.");
                }

                String[] activityDetails = selectedActivity.split("\\|");
                int labId = Integer.parseInt(activityDetails[0].split(":")[1].trim());
                int activityId = Integer.parseInt(activityDetails[1].split(":")[1].trim());

                String timeSlotDetails = activityDetails[3].split(": ")[1].trim();

                String timeSlot = timeSlotDetails.replaceAll("\\[|\\]", ""); // Remove brackets if any
                timeSlot =  timeSlot.split(" - ")[0].trim();     // Format to [XX:XX]

                activityManager.joinActivity(userId, labId, activityId, timeSlot);
                JOptionPane.showMessageDialog(parent, "Successfully joined the activity!");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Invalid User ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
