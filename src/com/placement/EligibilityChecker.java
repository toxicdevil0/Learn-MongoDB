package com.placement;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoDatabase;

public class EligibilityChecker {

    public static String checkEligibility(
            String studentId,
            String driveId) {

        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();

            // =========================================
            // GET STUDENT
            // =========================================

            Document student =
                    database.getCollection("students")
                            .find(
                                new Document(
                                    "studentId",
                                    studentId
                                )
                            )
                            .first();

            if (student == null) {

                return "Student not found.";
            }


            // =========================================
            // GET JOB DRIVE
            // =========================================

            Document drive =
                    database.getCollection("job_drives")
                            .find(
                                new Document(
                                    "driveId",
                                    driveId
                                )
                            )
                            .first();

            if (drive == null) {

                return "Job drive not found.";
            }


            // =========================================
            // CHECK CGPA
            // =========================================

            Number studentCGPANumber =
                    student.get(
                        "cgpa",
                        Number.class
                    );

            Number eligibilityCGPANumber =
                    drive.get(
                        "eligibilityCGPA",
                        Number.class
                    );

            if (studentCGPANumber == null ||
                eligibilityCGPANumber == null) {

                return "CGPA information is missing.";
            }

            double studentCGPA =
                    studentCGPANumber.doubleValue();

            double eligibilityCGPA =
                    eligibilityCGPANumber.doubleValue();


            if (studentCGPA < eligibilityCGPA) {

                return "NOT ELIGIBLE: CGPA requirement not satisfied.";
            }


            // =========================================
            // CHECK DEPARTMENT
            // =========================================

            Document eligibility =
                    database.getCollection("companies")
                            .find(
                                new Document(
                                    "companyId",
                                    drive.getString("companyId")
                                )
                            )
                            .projection(
                                new Document(
                                    "eligibility",
                                    1
                                )
                            )
                            .first();

            if (eligibility != null) {

                Document eligibilityData =
                        (Document)
                        eligibility.get("eligibility");

                if (eligibilityData != null) {

                    List<String> allowedDepartments =
                            (List<String>)
                            eligibilityData.get(
                                "allowedDepartments"
                            );

                    String studentDepartment =
                            student.getString(
                                "department"
                            );

                    if (allowedDepartments != null &&
                        !allowedDepartments.isEmpty()) {

                        if (!allowedDepartments.contains(
                                studentDepartment)) {

                            return "NOT ELIGIBLE: Department is not allowed.";
                        }
                    }
                }
            }


            // =========================================
            // CHECK SKILLS
            // =========================================

            List<String> requiredSkills =
                    (List<String>)
                    drive.get("skillsRequired");

            List<String> studentSkills =
                    (List<String>)
                    student.get("skills");

            if (requiredSkills != null &&
                !requiredSkills.isEmpty()) {

                if (studentSkills == null) {

                    return "NOT ELIGIBLE: Required skills are missing.";
                }

                for (String requiredSkill :
                        requiredSkills) {

                    boolean found = false;

                    for (String studentSkill :
                            studentSkills) {

                        if (studentSkill.equalsIgnoreCase(
                                requiredSkill)) {

                            found = true;
                            break;
                        }
                    }

                    if (!found) {

                        return "NOT ELIGIBLE: Missing required skill - "
                                + requiredSkill;
                    }
                }
            }


            // =========================================
            // ALL CONDITIONS PASSED
            // =========================================

            return "ELIGIBLE";

        } catch (Exception e) {

            e.printStackTrace();

            return "Error checking eligibility.";
        }
    }
}