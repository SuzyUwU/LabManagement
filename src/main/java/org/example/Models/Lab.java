package org.example.Models;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Lab {
    private final MongoCollection<Document> labCollection;

    public Lab() {
        String uri = "mongodb://localhost:27017"; // Replace with your MongoDB URI
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("LabManagementDB");

        if (!collectionExists(database)) {
            database.createCollection("Labs");
        }
        labCollection = database.getCollection("Labs");
    }

    private boolean collectionExists(MongoDatabase database) {
        for (String name : database.listCollectionNames()) {
            if (name.equals("Labs")) {
                return true;
            }
        }
        return false;
    }

    public void addLab(int labId, String labName, String labFunction, String specification, String location) {
        Document lab = new Document("labId", labId)
                .append("labName", labName)
                .append("labFunction", labFunction)
                .append("specification", specification)
                .append("location", location);
        labCollection.insertOne(lab);
    }

    public List<Document> getAllLabs() {
        List<Document> labs = new ArrayList<>();
        labCollection.find().iterator().forEachRemaining(labs::add);
        return labs;
    }

}

