package org.example.Models;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private int activityId;
    private String activityName;
    private int labId;
    private boolean[] hours;
    private MongoCollection<Document> activityCollection;
    private MongoCollection<Document> userActivitiesCollection;

    private static final String[] TIME_SLOTS = {
            "08:00",
            "09:00",
            "13:00",
            "15:00"
    };
    public Activity() {
        initializeCollections();
    }

    public Activity(int activityId, String activityName, int labId, String description, boolean[] hours) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.labId = labId;
        this.hours = hours;
        initializeCollections();
    }

    private void initializeCollections() {
        String uri = "mongodb+srv://EmilyLuzlieSuzy:Aku201705@lab.xqywn.mongodb.net/?retryWrites=true&w=majority&appName=Lab"; // MongoDB connection URI

        try {
            MongoClient mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase("LabManagementDB");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
        }
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("LabManagementDB");
        if (collectionExists(database, "Activities")) {
            database.createCollection("Activities");
        }
        activityCollection = database.getCollection("Activities");

        if (collectionExists(database, "UserActivities")) {
            database.createCollection("UserActivities");
        }
        userActivitiesCollection = database.getCollection("UserActivities");

    }

    private boolean collectionExists(MongoDatabase database, String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                return false;
            }
        }
        return true;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getLabId() {
        return labId;
    }

    public List<String> getActiveTimeSlots() {
        List<String> activeSlots = new ArrayList<>();
        for (int i = 0; i < hours.length; i++) {
            if (hours[i]) {
                activeSlots.add(TIME_SLOTS[i]);
            }
        }
        return activeSlots;
    }
    public boolean hasJoinedActivity(long userId, int labId, String activityName) {
        try {
            Document query = new Document("userId", userId)
                    .append("labId", labId)
                    .append("activityName", activityName);

            Document result = userActivitiesCollection.find(query).first();

            return result != null;
        } catch (Exception e) {
            System.err.println("Error while checking joined activity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelActivity(String activityName, long userId) {
        Document query = new Document("activityName", activityName);
        if (userId != 0) {
            query.append("userId", userId);
        }
        DeleteResult result = userActivitiesCollection.deleteOne(query);
        return result.getDeletedCount() > 0;
    }

    public void joinActivity(long userId, int labId, int activityId, String timeSlot) throws Exception {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            throw new Exception("Time slot cannot be null or empty.");
        }

        timeSlot = timeSlot.trim();

        int timeSlotIndex = getTimeSlotIndex(timeSlot);

        Document query = new Document("labId", labId)
                .append("activityId", activityId)
                .append("hours." + timeSlotIndex, true);

        Document activityDoc = activityCollection.find(query).first();

        if (activityDoc == null) {
            throw new Exception("The selected time slot is not active for this activity.");
        }

        List<Integer> participants = activityDoc.getList("participants", Integer.class);
        if (participants.contains((int) userId)) {
            throw new Exception("You are already registered for this activity.");
        }

        participants.add((int) userId);
        activityCollection.updateOne(
                new Document("activityId", activityId),
                new Document("$set", new Document("participants", participants))
        );

        Document userActivity = new Document("userId", userId)
                .append("labId", labId)
                .append("activityId", activityId)
                .append("timeSlot", timeSlot)
                .append("activityName", activityDoc.getString("activityName"));

        userActivitiesCollection.insertOne(userActivity);
    }

    private int getTimeSlotIndex(String timeSlot) throws Exception {
        if (timeSlot == null || timeSlot.isEmpty()) {
            throw new Exception("Time slot cannot be null or empty.");
        }

        timeSlot = timeSlot.trim();
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(timeSlot)) {
                return i;
            }
        }

        throw new Exception("Invalid time slot: " + timeSlot);
    }


    public boolean addActivity(String activityName, int labId, String description, boolean[] hours) {
        Document query = new Document("labId", labId).append("activityName", activityName);
        if (activityCollection.find(query).first() != null) {
            return false;
        }

        int newActivityId = (int) (activityCollection.countDocuments() + 1);
        Document activity = new Document("activityId", newActivityId)
                .append("activityName", activityName)
                .append("labId", labId)
                .append("description", description)
                .append("hours", convertArrayToList(hours))
                .append("participants", new ArrayList<Integer>());

        try {
            activityCollection.insertOne(activity);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to add activity: " + e.getMessage());
            return false;
        }
    }

    private List<Boolean> convertArrayToList(boolean[] hours) {
        List<Boolean> list = new ArrayList<>();
        for (boolean hour : hours) {
            list.add(hour);
        }
        return list;
    }

    public List<Activity> getAvailableActivity() {
        List<Activity> activities = new ArrayList<>();

        try (MongoCursor<Document> cursor = activityCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int activityId = doc.getInteger("activityId");
                String activityName = doc.getString("activityName");
                int labId = doc.getInteger("labId");
                String description = doc.getString("description");

                List<Boolean> hoursList = (List<Boolean>) doc.get("hours");
                boolean[] hours = null;
                if (hoursList != null) {
                    hours = new boolean[hoursList.size()];
                    for (int i = 0; i < hoursList.size(); i++) {
                        hours[i] = hoursList.get(i);
                    }
                }

                activities.add(new Activity(activityId, activityName, labId, description, hours));
            }
        }

        return activities;
    }
}
