package org.example.View.SideMenu;

import javax.swing.*;
import java.awt.*;
import org.example.Models.Activity;

public class AddActivityGUI {
    public static void showDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField activityNameField = new JTextField();
        JTextField labIdField = new JTextField();
        JTextField descriptionField = new JTextField();

        JCheckBox hour1 = new JCheckBox("08:00 - 10:00");
        JCheckBox hour2 = new JCheckBox("09:00 - 11:30");
        JCheckBox hour3 = new JCheckBox("13:00 - 15:00");
        JCheckBox hour4 = new JCheckBox("15:00 - 17:00");

        panel.add(new JLabel("Activity Name:"));
        panel.add(activityNameField);
        panel.add(new JLabel("Lab ID:"));
        panel.add(labIdField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(hour1);
        panel.add(hour2);
        panel.add(hour3);
        panel.add(hour4);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Activity", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String activityName = activityNameField.getText();
            int labId = Integer.parseInt(labIdField.getText());
            String description = descriptionField.getText();

            // Prepare the hours array based on user input
            boolean[] hours = new boolean[]{
                    hour1.isSelected(),
                    hour2.isSelected(),
                    hour3.isSelected(),
                    hour4.isSelected()
            };

            // Call the addActivity function
            Activity activityModel = new Activity();
            boolean success = activityModel.addActivity(activityName, labId, description, hours);
            if (success) {
                JOptionPane.showMessageDialog(null, "Activity added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add activity. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
