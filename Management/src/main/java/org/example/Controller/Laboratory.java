package org.example.Controller;

import org.example.Models.Lab;
import org.example.Models.Activity;

import java.util.ArrayList;
import java.util.List;

public class Laboratory {
    public static final List<Lab> LabList = new ArrayList<>();

    public void addLab(Lab labs){
        LabList.add(labs);
    }
    public static void main(String[] args) {
        Activity activityManager = new Activity();

        // Define hours for the activities
        boolean[] hours1 = {true, false, false, false}; // Example time slots
        boolean[] hours2 = {false, true, false, false};
        boolean[] hours3 = {false, false, true, false};
        boolean[] hours4 = {false, false, false, true};

        // Add activities for Networking Lab
        activityManager.addActivity("Router Configuration", 1, "Configure and manage routers.", hours1);
        activityManager.addActivity("Network Security Basics", 1, "Introduction to network security.", hours2);
        activityManager.addActivity("VPN Setup", 1, "Set up a secure VPN.", hours3);
        activityManager.addActivity("Wireless Networking", 1, "Learn about wireless networking.", hours4);

        // Add activities for System Lab
        activityManager.addActivity("System Design Patterns", 2, "Learn common design patterns.", hours1);
        activityManager.addActivity("Server Deployment", 2, "Deploy and manage servers.", hours2);
        activityManager.addActivity("System Monitoring", 2, "Techniques for system monitoring.", hours3);
        activityManager.addActivity("Cloud Computing Basics", 2, "Introduction to cloud computing.", hours4);

        // Add activities for Multimedia Lab
        activityManager.addActivity("Video Editing", 3, "Basics of video editing.", hours1);
        activityManager.addActivity("Graphic Design", 3, "Learn graphic design tools.", hours2);
        activityManager.addActivity("Animation Basics", 3, "Introduction to animation.", hours3);
        activityManager.addActivity("Audio Mixing", 3, "Learn audio mixing techniques.", hours4);

        // Add activities for Software Engineering Lab
        activityManager.addActivity("Agile Development", 4, "Overview of Agile methodology.", hours1);
        activityManager.addActivity("Version Control with Git", 4, "Learn Git for version control.", hours2);
        activityManager.addActivity("Unit Testing", 4, "Basics of unit testing.", hours3);
        activityManager.addActivity("CI/CD Pipelines", 4, "Setup continuous integration pipelines.", hours4);

        System.out.println("All activities added successfully.");
    }
}
