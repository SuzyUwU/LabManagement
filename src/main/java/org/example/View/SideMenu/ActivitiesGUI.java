package org.example.View.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import org.example.Models.Activity;
import org.example.Models.User;

public class ActivitiesGUI {

    public static void showActivities(List<Activity> allActivities, User user) {
        JFrame frame = new JFrame("Activities");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        DefaultListModel<String> activityListModel = new DefaultListModel<>();
        JList<String> activityList = new JList<>(activityListModel);
        activityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activityList.setBorder(BorderFactory.createTitledBorder("Available Activities"));

        JComboBox<String> filterDropdown = new JComboBox<>(new String[]{"All Activities", "Joined Activities"});
        filterDropdown.addActionListener(e -> {
            String selectedFilter = (String) filterDropdown.getSelectedItem();
            List<Activity> filteredActivities;

            if ("Joined Activities".equals(selectedFilter)) {
                filteredActivities = allActivities.stream()
                        .filter(activity -> activity.hasJoinedActivity(user.getUserId(), activity.getLabId(), activity.getActivityName()))
                        .collect(Collectors.toList());
            } else {
                filteredActivities = allActivities;
            }

            updateActivityList(filteredActivities, activityListModel);
        });

        updateActivityList(allActivities, activityListModel);

        JButton cancelActivityButton = new JButton("Cancel Activity");
        cancelActivityButton.addActionListener(e -> {
            int selectedIndex = activityList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedFilter = (String) filterDropdown.getSelectedItem();
                List<Activity> displayedActivities = "Joined Activities".equals(selectedFilter)
                        ? allActivities.stream()
                        .filter(activity -> activity.hasJoinedActivity(user.getUserId(), activity.getLabId(), activity.getActivityName()))
                        .toList()
                        : allActivities;

                Activity selectedActivity = displayedActivities.get(selectedIndex);
                int confirmation = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to cancel participation in this activity?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    boolean success = selectedActivity.cancelActivity(selectedActivity.getActivityName(), user.getUserId());
                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Successfully canceled the activity!");
                        filterDropdown.getActionListeners()[0].actionPerformed(null);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to cancel the activity. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an activity first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(filterDropdown, BorderLayout.NORTH);
        panel.add(new JScrollPane(activityList), BorderLayout.CENTER);
        panel.add(cancelActivityButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void updateActivityList(List<Activity> activities, DefaultListModel<String> activityListModel) {
        activityListModel.clear();
        for (Activity activity : activities) {
            activityListModel.addElement(activity.getActivityName() + " (Lab ID: " + activity.getLabId() + ")");
        }
    }
}
