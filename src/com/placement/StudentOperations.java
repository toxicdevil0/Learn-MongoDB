package com.placement;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class StudentOperations {

    private MongoCollection<Document> students;

    public StudentOperations() {

        MongoDatabase database =
                MongoDBConnection.getDatabase();

        students =
                database.getCollection("students");
    }


    // CREATE
    public void addStudent() {

        Document address = new Document();

        address.append("city", "Jaipur");
        address.append("state", "Rajasthan");


        Document education = new Document();

        education.append("tenthPercentage", 90);
        education.append("twelfthPercentage", 87);


        Document student = new Document();

        student.append("studentId", "STU017");
        student.append("name", "Nikhil Sharma");
        student.append("email", "nikhil@gmail.com");
        student.append("phone", "9876500017");

        student.append("department", "IT");
        student.append("graduationYear", 2027);
        student.append("cgpa", 8.2);


        student.append(
            "skills",
            java.util.Arrays.asList(
                "Java",
                "MongoDB",
                "SQL"
            )
        );


        student.append(
            "certifications",
            java.util.Arrays.asList(
                "Java Programming",
                "MongoDB Basics"
            )
        );


        student.append(
            "address",
            address
        );


        student.append(
            "education",
            education
        );


        student.append(
            "placementStatus",
            "Not Placed"
        );


        students.insertOne(student);

        System.out.println(
            "Student added successfully!"
        );
    }


    // READ - ALL
    public void viewAllStudents() {

        System.out.println(
            "\n========== ALL STUDENTS =========="
        );

        for (Document student : students.find()) {

            System.out.println(
                student.toJson()
            );
        }
    }


    // READ - ONE
    public void findStudent(String studentId) {

        Document student =
                students.find(
                    new Document(
                        "studentId",
                        studentId
                    )
                ).first();

        if (student != null) {

            System.out.println(
                "\nStudent Found:"
            );

            System.out.println(
                student.toJson()
            );

        } else {

            System.out.println(
                "Student not found!"
            );
        }
    }


    // UPDATE
    public void updateCGPA(
            String studentId,
            double newCGPA) {

        students.updateOne(

            new Document(
                "studentId",
                studentId
            ),

            new Document(
                "$set",
                new Document(
                    "cgpa",
                    newCGPA
                )
            )
        );

        System.out.println(
            "CGPA updated successfully!"
        );
    }


    // DELETE
    public void deleteStudent(
            String studentId) {

        students.deleteOne(

            new Document(
                "studentId",
                studentId
            )
        );

        System.out.println(
            "Student deleted successfully!"
        );
    }
}