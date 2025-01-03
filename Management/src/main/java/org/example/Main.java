package org.example;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.Controller.AddLabs;
import org.example.Models.User;
import org.example.View.LoginMenuGUI;
import org.example.Models.Lab;

public class Main {

    public static void main(String[] args) {
        String connectionString = "mongodb+srv://EmilyLuzlieSuzy:Aku201705@lab.xqywn.mongodb.net/?retryWrites=true&w=majority&appName=Lab";
        MongoClient client = MongoClients.create("mongodb+srv://EmilyLuzlieSuzy:Aku201705@lab.xqywn.mongodb.net/?retryWrites=true&w=majority&appName=Lab");

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("myDatabase");

            MongoCollection<Document> collection = database.getCollection("myCollection");

            Document doc = new Document("name", "testDoc")
                    .append("type", "example")
                    .append("count", 1);
            collection.insertOne(doc);

            System.out.println("Database and collection created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginMenuGUI.ShowGUI();
    }
}