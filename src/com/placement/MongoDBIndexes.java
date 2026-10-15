package com.placement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDBIndexes {

    public static void createIndexes() {

        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();

            // =============================================
            // STUDENTS
            // =============================================

            MongoCollection<Document> students =
                    database.getCollection(
                            "students"
                    );

            students.createIndex(
                    new Document(
                            "studentId",
                            1
                    )
            );


            // =============================================
            // COMPANIES
            // =============================================

            MongoCollection<Document> companies =
                    database.getCollection(
                            "companies"
                    );

            companies.createIndex(
                    new Document(
                            "companyId",
                            1
                    )
            );


            // =============================================
            // JOB DRIVES
            // =============================================

            MongoCollection<Document> jobDrives =
                    database.getCollection(
                            "job_drives"
                    );

            jobDrives.createIndex(
                    new Document(
                            "driveId",
                            1
                    )
            );

            jobDrives.createIndex(
                    new Document(
                            "companyId",
                            1
                    )
            );


            // =============================================
            // APPLICATIONS
            // =============================================

            MongoCollection<Document> applications =
                    database.getCollection(
                            "applications"
                    );

            applications.createIndex(
                    new Document(
                            "applicationId",
                            1
                    )
            );

            applications.createIndex(
                    new Document(
                            "studentId",
                            1
                    )
            );

            applications.createIndex(
                    new Document(
                            "driveId",
                            1
                    )
            );

            applications.createIndex(
                    new Document(
                            "status",
                            1
                    )
            );


            System.out.println(
                    "MongoDB Indexes Created Successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating indexes: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}