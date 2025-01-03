package org.example.Models;

import com.mongodb.client.*;
import org.bson.Document;

public class User {
    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> counterCollection;
    private long currentUserId;

    public User() {
        String uri = "mongodb://localhost:27017"; // Replace with your MongoDB URI
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("UserProfileDB");

        if (collectionExists(database, "Users")) {
            database.createCollection("Users");
        }
        userCollection = database.getCollection("Users");

        if (collectionExists(database, "Counters")) {
            database.createCollection("Counters");
            MongoCollection<Document> counterCollection = database.getCollection("Counters");
            counterCollection.insertOne(new Document("_id", "userIdCounter").append("lastId", 23051204000L));
        }
        counterCollection = database.getCollection("Counters");

        MongoDatabase activityDatabase = mongoClient.getDatabase("LabManagementDB");

        if (collectionExists(activityDatabase, "Activities")) {
            activityDatabase.createCollection("Activities");
        }
        MongoCollection<Document> activityCollection = activityDatabase.getCollection("Activities");

        if (collectionExists(activityDatabase, "UserActivities")) {
            activityDatabase.createCollection("UserActivities");
        }
        MongoCollection<Document> userActivitiesCollection = activityDatabase.getCollection("UserActivities");
    }

    private boolean collectionExists(MongoDatabase database, String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                return false;
            }
        }
        return true;
    }

    private long getNextUserId() {
        Document counter = counterCollection.findOneAndUpdate(
                new Document("_id", "userIdCounter"),
                new Document("$inc", new Document("lastId", 1))
        );
        assert counter != null;
        return counter.getLong("lastId") + 1;
    }

    public boolean validateCredentials(long userId, String username, String password) {
        Document query = new Document();

        if (userId > 0) {
            query.append("userId", userId);
        } else if (username != null && !username.isEmpty()) {
            query.append("username", username);
        } else {
            return false;
        }

        Document user = userCollection.find(query).first();

        return user != null && user.getString("password").equals(password);
    }

    public void addUser(String username, String password) {
        if (userCollection.find(new Document("username", username)).first() != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        long newUserId = getNextUserId();
        Document user = new Document("userId", newUserId)
                .append("username", username)
                .append("password", password);
        userCollection.insertOne(user);
    }

    public Document getUser(long userId, String username) {
        Document query = new Document();

        if (userId > 0) {
            query.append("userId", userId);
        } else if (username != null && !username.isEmpty()) {
            query.append("username", username);
        }

        Document user = userCollection.find(query).first();

        if (user != null) {
            currentUserId = user.getLong("userId");
        }

        return userCollection.find(query).first();
    }

    public long getUserId() {
        return currentUserId;
    }
}