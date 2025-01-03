package org.example.Models;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Lab {
    private final MongoCollection<Document> labCollection;

    public Lab() {
        String uri = "mongodb+srv://EmilyLuzlieSuzy:Aku201705@lab.xqywn.mongodb.net/?retryWrites=true&w=majority&appName=Lab";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("LabManagementDB");

        // Create 'Labs' collection if it doesn't exist
        if (!collectionExists(database, "Labs")) {
            database.createCollection("Labs");
        }
        labCollection = database.getCollection("Labs");
    }

    // Check if a collection exists in the database
    private boolean collectionExists(MongoDatabase database, String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                return true;
            }
        }
        return false;
    }

    // Add a new lab to the database
    public void addLab(int labId, String labName, String labFunction, String specification, String location) {
        Document lab = new Document("labId", labId)
                .append("labName", labName)
                .append("labFunction", labFunction)
                .append("specification", specification)
                .append("location", location);
        labCollection.insertOne(lab);
        System.out.println("Lab added with ID: " + labId);
    }

    // Retrieve a lab by its ID
    public Document getLabById(int labId) {
        return labCollection.find(new Document("labId", labId)).first();
    }

    // Update a lab's details
    public void updateLab(int labId, String field, String newValue) {
        Document update = new Document("$set", new Document(field, newValue));
        labCollection.updateOne(new Document("labId", labId), update);
        System.out.println("Lab with ID " + labId + " updated.");
    }

    // Delete a lab by its ID
    public void deleteLab(int labId) {
        labCollection.deleteOne(new Document("labId", labId));
        System.out.println("Lab with ID " + labId + " deleted.");
    }

    public List<Document> getAllLabs() {
        List<Document> labs = new ArrayList<>();
        labCollection.find().iterator().forEachRemaining(labs::add);
        return labs;
    }
    public String getLabName(Document lab) {
        return lab.getString("labName");
    }

    public int getLabId(Document lab) {
        return lab.getInteger("labId");
    }
}

