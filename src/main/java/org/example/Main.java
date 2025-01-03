package org.example;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.example.View.Menus.LoginMenuGUI;

public class Main {

    public static void main(String[] args) {
        String connectionString = "mongodb://localhost:27017"; // Replace with your MongoDB URI

        try (MongoClient ignored = MongoClients.create(connectionString)) {
            System.out.println("Database and collection created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginMenuGUI.ShowGUI();
    }
}