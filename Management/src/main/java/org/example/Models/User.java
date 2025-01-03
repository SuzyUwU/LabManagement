package org.example.Models;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

public class User {
    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> counterCollection;
    private final MongoCollection<Document> activityCollection;
    private final MongoCollection<Document> userActivitiesCollection;
    private long userId;
    private long currentUserId;

    public User() {
        String uri = "mongodb+srv://EmilyLuzlieSuzy:Aku201705@lab.xqywn.mongodb.net/?retryWrites=true&w=majority&appName=Lab";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("UserProfileDB");

        // Initialize collections
        if (!collectionExists(database, "Users")) {
            database.createCollection("Users");
        }
        userCollection = database.getCollection("Users");

        if (!collectionExists(database, "Counters")) {
            database.createCollection("Counters");
            MongoCollection<Document> counterCollection = database.getCollection("Counters");
            counterCollection.insertOne(new Document("_id", "userIdCounter").append("lastId", 23051204000L));
        }
        counterCollection = database.getCollection("Counters");

        MongoDatabase activityDatabase = mongoClient.getDatabase("LabManagementDB");

        if (!collectionExists(activityDatabase, "Activities")) {
            activityDatabase.createCollection("Activities");
        }
        activityCollection = activityDatabase.getCollection("Activities");

        if (!collectionExists(activityDatabase, "UserActivities")) {
            activityDatabase.createCollection("UserActivities");
        }
        userActivitiesCollection = activityDatabase.getCollection("UserActivities");
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
    public boolean hasJoinedActivity(int labId) {
        try {
            System.out.println("Using collection: " + activityCollection.getNamespace().getCollectionName());
            Document query = new Document("userId", getUserId()).append("labId", labId);
            System.out.println("Querying with: " + query.toJson());

            Document result = activityCollection.find(query).first();
            if (result != null) {
                System.out.println("Found match: " + result.toJson());
                return true;
            } else {
                System.out.println("No match found for query: " + query.toJson());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error while checking joined activity: " + e.getMessage());
            return false;
        }
    }


    public boolean cancelActivity(long userId, int activityId) {
        try {
            // Query the database to remove the user from the activity
            Document query = new Document("userId", userId).append("activityId", activityId);
            DeleteResult result = activityCollection.deleteOne(query);

            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error while canceling activity: " + e.getMessage());
            return false;
        }
    }

    // Generate the next user ID
    private long getNextUserId() {
        Document counter = counterCollection.findOneAndUpdate(
                new Document("_id", "userIdCounter"),
                new Document("$inc", new Document("lastId", 1))
        );
        return counter.getLong("lastId") + 1;
    }

    // Validate user credentials
    public boolean validateCredentials(long userId, String username, String password) {
        Document query = new Document();

        if (userId > 0) {
            query.append("userId", userId);
        } else if (username != null && !username.isEmpty()) {
            query.append("username", username);
        } else {
            return false; // Neither ID nor username provided
        }

        Document user = userCollection.find(query).first();

        return user != null && user.getString("password").equals(password);
    }

    // Check if a user exists by userId
    public boolean userExists(long userId) {
        return userCollection.find(new Document("userId", userId)).first() != null;
    }

    // Add a new user
    public void addUser(String username, String password) {
        if (userCollection.find(new Document("username", username)).first() != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        long newUserId = getNextUserId();
        Document user = new Document("userId", newUserId)
                .append("username", username)
                .append("password", password); // Consider hashing the password for security
        userCollection.insertOne(user);
        System.out.println("User added with ID: " + newUserId);
    }

    // Fetch user details
    public Document getUser(long userId, String username) {
        Document query = new Document();

        if (userId > 0) {
            query.append("userId", userId);
        } else if (username != null && !username.isEmpty()) {
            query.append("username", username);
        }

        Document user = userCollection.find(query).first();

        if (user != null) {
            // Extract and assign the userId to currentUserId
            currentUserId = user.getLong("userId");
            System.out.println("Found userId: " + currentUserId);
        } else {
            System.out.println("No user found.");
        }

        return userCollection.find(query).first();
    }

    // Join activity function
    public boolean joinActivity(long userId, int activityId, String timeSlot) {
        // Check if the activity exists
        Document activity = activityCollection.find(new Document("activityId", activityId)).first();
        if (activity == null) {
            System.out.println("Activity not found.");
            return false;
        }

        // Check if the user is already enrolled in an overlapping activity
        Document conflict = userActivitiesCollection.find(
                new Document("userId", userId)
                        .append("timeSlot", timeSlot)
        ).first();

        if (conflict != null) {
            System.out.println("User already enrolled in an overlapping activity.");
            return false;
        }

        // Enroll user in the activity
        Document userActivity = new Document("userId", userId)
                .append("activityId", activityId)
                .append("timeSlot", timeSlot)
                .append("activityName", activity.getString("activityName"));

        userActivitiesCollection.insertOne(userActivity);
        System.out.println("User successfully joined the activity.");
        return true;
    }

    public long getUserId() {
        System.out.println(currentUserId);
        return currentUserId;
    }
}
