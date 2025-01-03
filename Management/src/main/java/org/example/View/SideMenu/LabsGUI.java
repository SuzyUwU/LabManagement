package org.example.View.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.example.Models.Lab;
import org.example.Models.Activity;

public class LabsGUI {

    public static void showLabs(Lab labModel, List<Activity> activities) {
        JFrame frame = new JFrame("Labs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        // Fetch labs from the database
        List<Document> labDocuments = labModel.getAllLabs();

        // Populate the list model with unique lab names
        DefaultListModel<String> labListModel = new DefaultListModel<>();
        List<String> addedLabNames = new ArrayList<>();
        for (Document lab : labDocuments) {
            String labName = lab.getString("labName");
            if (!addedLabNames.contains(labName)) {
                labListModel.addElement(labName);
                addedLabNames.add(labName);
            }
        }

        JList<String> labList = new JList<>(labListModel);
        labList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        labList.setBorder(BorderFactory.createTitledBorder("Labs"));

        JTextArea activityDetails = new JTextArea();
        activityDetails.setEditable(false);
        activityDetails.setBorder(BorderFactory.createTitledBorder("Lab Activities"));

        JButton viewActivitiesButton = new JButton("View Activities");
        viewActivitiesButton.addActionListener(e -> {
            int selectedIndex = labList.getSelectedIndex();
            if (selectedIndex != -1) {
                Activity activityManager = new Activity();

                Document selectedLab = labDocuments.get(selectedIndex);
                int selectedLabId = selectedLab.getInteger("labId");
                StringBuilder details = new StringBuilder("Activities for " + selectedLab.getString("labName") + ":\n");

                boolean hasActivities = false;

                for (Activity activity : activities) {

                    // Check if the activity matches the selected lab
                    if (activity.getLabId() == selectedLabId) {
                        details.append("- ").append(activity.getActivityName()).append("\n");
                        hasActivities = true;
                    }
                }

                if (!hasActivities) {
                    details.append("No activities found.");
                }

                activityDetails.setText(details.toString());
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a lab first.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(labList), BorderLayout.WEST);
        panel.add(activityDetails, BorderLayout.CENTER);
        panel.add(viewActivitiesButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
