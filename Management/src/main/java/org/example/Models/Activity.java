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
    private String description;
    private boolean[] hours;

    private MongoCollection<Document> activityCollection;
    private MongoCollection<Document> userActivitiesCollection;

    private static final String[] TIME_SLOTS = {
            "08:00 - 10:00",
            "09:00 - 11:30",
            "13:00 - 15:00",
            "15:00 - 17:00"
    };
    public Activity() {
        initializeCollections();
    }

    // Constructor for Activity
    public Activity(int activityId, String activityName, int labId, String description, boolean[] hours) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.labId = labId;
        this.description = description;
        this.hours = hours;
        initializeCollections();
    }

    // Initialize collections
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
        if (!collectionExists(database, "Activities")) {
            database.createCollection("Activities");
        }
        activityCollection = database.getCollection("Activities");

        if (!collectionExists(database, "UserActivities")) {
            database.createCollection("UserActivities");
        }
        userActivitiesCollection = database.getCollection("UserActivities");

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

    // Getters
    public int getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getLabId() {
        return labId;
    }

    public String getDescription() {
        return description;
    }

    public boolean[] getHours() {
        return hours;
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
    @Deprecated
    private int getTimeSlotIndexs(String timeSlot) throws Exception {
        String[] TIME_SLOTS = {
                "08:00 - 10:00",
                "09:00 - 11:30",
                "13:00 - 15:00",
                "15:00 - 17:00"
        };

        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(timeSlot)) {
                return i;
            }
        }

        throw new Exception("Invalid time slot: " + timeSlot);
    }
    public boolean hasJoinedActivity(long userId, int labId) {
        try {
            Document query = new Document("userId", userId).append("labId", labId);
            Document result = userActivitiesCollection.find(query).first();
            return result != null;
        } catch (Exception e) {
            System.err.println("Error while checking joined activity: " + e.getMessage());
            return false;
        }
    }

    // Cancel a user's activity enrollment
    public boolean cancelActivity(long userId, int activityId) {
        try {
            // Construct the query
            Document query = new Document("userId", userId).append("activityId", activityId);
            System.out.println("Query for cancellation: " + query.toJson());

            // Attempt deletion
            DeleteResult result = userActivitiesCollection.deleteOne(query);
            System.out.println("Delete result: " + result);

            // Check deletion status
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error while canceling activity: " + e.getMessage());
            return false;
        }
    }
    private int getTimeSlotIndex(String timeSlot) throws Exception {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(timeSlot)) {
                return i;
            }
        }
        throw new Exception("Invalid time slot: " + timeSlot);
    }

    private boolean isTimeSlotActive(boolean[] hours, String timeSlot) throws Exception {
        int index = getTimeSlotIndex(timeSlot);
        return hours[index];
    }

    // Join activity function
    public void joinActivity(long userId, int labId, String timeSlot) throws Exception {
        System.out.println("Received labId: " + labId);
        System.out.println("Received time slot: [" + timeSlot + "]");

        // Normalize the input timeSlot
        timeSlot = timeSlot.replaceAll("\\[|\\]", "").trim();
        System.out.println("Normalized time slot: [" + timeSlot + "]");

        // Resolve the timeSlot index and validate that it's active
        int timeSlotIndex = getTimeSlotIndex(timeSlot);
        System.out.println("Resolved time slot index: " + timeSlotIndex);

        // Query the activity by labId and timeSlot
        System.out.println("Querying activityCollection with labId: " + labId + " and timeSlot: " + timeSlot);
        Document activity = activityCollection.find(
                new Document("labId", labId).append("timeSlots", timeSlot)
        ).first();

        if (activity == null) {
            throw new Exception("The selected activity does not exist or is not active during the specified time slot.");
        }

        // Check if the selected time slot is active for this activity
        boolean[] hours = (boolean[]) activity.get("hours");
        if (!isTimeSlotActive(hours, timeSlot)) {
            throw new Exception("The selected time slot is not active for this activity.");
        }

        // Check for conflicts with other activities
        System.out.println("Checking for conflicts with User ID: " + userId + ", Time Slot: " + timeSlot);
        Document conflictingActivity = userActivitiesCollection.find(
                new Document("userId", userId).append("timeSlot", timeSlot)
        ).first();

        if (conflictingActivity != null) {
            throw new Exception("You are already enrolled in another activity during this time slot.");
        }

        // Fetch and validate the activity name
        String activityName = activity.getString("activityName");
        if (activityName == null || activityName.isEmpty()) {
            throw new Exception("Activity name is missing for labId: " + labId);
        }
        System.out.println("Resolved activity name: " + activityName);

        // Add the user to the activity
        Document userActivity = new Document("userId", userId)
                .append("labId", labId)
                .append("activityName", activityName)
                .append("timeSlot", timeSlot);

        userActivitiesCollection.insertOne(userActivity);
        System.out.println("Successfully joined activity: " + activityName);
    }

    public boolean addActivity(String activityName, int labId, String description, boolean[] hours) {
        Document query = new Document("labId", labId).append("activityName", activityName);
        if (activityCollection.find(query).first() != null) {
            System.out.println("Activity with the same name already exists for this lab.");
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
            System.out.println("Activity added successfully: " + activityName);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to add activity: " + e.getMessage());
            return false;
        }
    }
    public void printAllActivities() {
        System.out.println("Fetching all activities from the database...");

        try {
            // Iterate through each document in the Activities collection
            for (Document doc : activityCollection.find()) {
                // Print the JSON representation of the activity document
                System.out.println(doc.toJson());
            }
        } catch (Exception e) {
            // Handle any exceptions that occur
            System.err.println("Error while fetching activities: " + e.getMessage());
        }
    }

    // Retrieve available activities
    public List<Activity> getAvailableActivity() {
        List<Activity> activities = new ArrayList<>();
        MongoCursor<Document> cursor = activityCollection.find().iterator();

        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int activityId = doc.getInteger("activityId");
                String activityName = doc.getString("activityName");
                int labId = doc.getInteger("labId");
                String description = doc.getString("description");

                // Fetch hours as List<Boolean> and convert to boolean[]
                List<Boolean> hoursList = (List<Boolean>) doc.get("hours");
                boolean[] hours = null;
                if (hoursList != null) {
                    hours = new boolean[hoursList.size()];
                    for (int i = 0; i < hoursList.size(); i++) {
                        hours[i] = hoursList.get(i);
                    }
                }

                // Add new Activity object to the list
                activities.add(new Activity(activityId, activityName, labId, description, hours));
            }
        } finally {
            cursor.close();
        }

        return activities;
    }
}
