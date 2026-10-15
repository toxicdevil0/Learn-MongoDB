package com.placement;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static MongoClient client;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {

        if (database == null) {

        	String uri = "mongodb+srv://vasanth970399_db_user:jP32ZdDMb2yu4MCf@student.doteys4.mongodb.net/?appName=student";

            client = MongoClients.create(uri);

            database = client.getDatabase("Poornima");
        }

        return database;
    }

    public static void closeConnection() {

        if (client != null) {
            client.close();
        }
    }
}