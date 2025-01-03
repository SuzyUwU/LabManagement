package org.example.Controller;

import org.example.Models.Lab;

public class AddLabs {
    public static void main(String[] args) {
        Lab labManager = new Lab();

        labManager.addLab(1, "Networking", "Tech Building", "Networking equipment and setup", "Tech Building");
        labManager.addLab(2, "System", "Tech Building", "System analysis and design tools", "Tech Building");
        labManager.addLab(3, "Multimedia", "Tech Building", "Multimedia tools and resources", "Tech Building");
        labManager.addLab(4, "Software Engineering", "Tech Building", "Software development tools", "Tech Building");

        System.out.println("Labs added successfully.");
    }
}
