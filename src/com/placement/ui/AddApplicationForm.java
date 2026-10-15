
package com.placement.ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;
import com.placement.EligibilityChecker;

public class AddApplicationForm extends JFrame {

    private ApplicationPanel parentPanel;

    private JTextField applicationIdField;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> driveComboBox;
    private JTextField dateField;
    private JComboBox<String> statusComboBox;

    private MongoDatabase database;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AddApplicationForm(ApplicationPanel parentPanel) {

        this.parentPanel = parentPanel;

        database = MongoDBConnection.getDatabase();

        setTitle("ADD APPLICATION");

        setSize(600, 500);

        setLocationRelativeTo(parentPanel);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createForm();

        loadStudents();

        loadJobDrives();
    }


    // =====================================================
    // CREATE FORM
    // =====================================================

    private void createForm() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );


        // =================================================
        // TITLE
        // =================================================

        JLabel titleLabel =
                new JLabel(
                        "ADD APPLICATION",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );


        // =================================================
        // FORM
        // =================================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                15
                        )
                );


        // -------------------------------------------------
        // APPLICATION ID
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Application ID:")
        );

        applicationIdField =
                new JTextField();

        formPanel.add(
                applicationIdField
        );


        // -------------------------------------------------
        // STUDENT
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Student:")
        );

        studentComboBox =
                new JComboBox<>();

        formPanel.add(
                studentComboBox
        );


        // -------------------------------------------------
        // JOB DRIVE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Job Drive:")
        );

        driveComboBox =
                new JComboBox<>();

        formPanel.add(
                driveComboBox
        );


        // -------------------------------------------------
        // APPLICATION DATE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Application Date:")
        );

        dateField =
                new JTextField();

        String today =
                new SimpleDateFormat(
                        "yyyy-MM-dd"
                ).format(
                        new Date()
                );

        dateField.setText(today);

        formPanel.add(
                dateField
        );


        // -------------------------------------------------
        // STATUS
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Status:")
        );

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Applied",
                                "Shortlisted",
                                "Rejected",
                                "Selected",
                                "Withdrawn"
                        }
                );

        formPanel.add(
                statusComboBox
        );


        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );


        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttonPanel =
                new JPanel();

        JButton addButton =
                new JButton(
                        "ADD APPLICATION"
                );

        JButton cancelButton =
                new JButton(
                        "CANCEL"
                );

        buttonPanel.add(
                addButton
        );

        buttonPanel.add(
                cancelButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // EVENTS
        // =================================================

        addButton.addActionListener(
                e -> addApplication()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );


        add(mainPanel);
    }


    // =====================================================
    // LOAD STUDENTS
    // =====================================================

    private void loadStudents() {

        try {

            MongoCollection<Document> students =
                    database.getCollection(
                            "students"
                    );

            studentComboBox.removeAllItems();

            for (Document student :
                    students.find()) {

                String studentId =
                        student.getString(
                                "studentId"
                        );

                String name =
                        student.getString(
                                "name"
                        );

                if (studentId != null) {

                    if (name != null) {

                        studentComboBox.addItem(
                                studentId
                                        + " - "
                                        + name
                        );

                    } else {

                        studentComboBox.addItem(
                                studentId
                        );
                    }
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading students:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =====================================================
    // LOAD JOB DRIVES
    // =====================================================

    private void loadJobDrives() {

        try {

            MongoCollection<Document> drives =
                    database.getCollection(
                            "job_drives"
                    );

            driveComboBox.removeAllItems();

            for (Document drive :
                    drives.find()) {

                String driveId =
                        drive.getString(
                                "driveId"
                        );

                String role =
                        drive.getString(
                                "role"
                        );

                String companyId =
                        drive.getString(
                                "companyId"
                        );

                if (driveId != null) {

                    String displayText =
                            driveId;

                    if (role != null) {

                        displayText +=
                                " - " + role;
                    }

                    if (companyId != null) {

                        displayText +=
                                " (" + companyId + ")";
                    }

                    driveComboBox.addItem(
                            displayText
                    );
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading job drives:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =====================================================
    // ADD APPLICATION
    // =====================================================

    private void addApplication() {

        try {

            // =================================================
            // APPLICATION ID
            // =================================================

            String applicationId =
                    applicationIdField
                            .getText()
                            .trim();

            if (applicationId.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter Application ID."
                );

                applicationIdField.requestFocus();

                return;
            }


            // =================================================
            // APPLICATION COLLECTION
            // =================================================

            MongoCollection<Document> applications =
                    database.getCollection(
                            "applications"
                    );


            // =================================================
            // CHECK DUPLICATE APPLICATION ID
            // =================================================

            Document existingApplication =
                    applications.find(
                            new Document(
                                    "applicationId",
                                    applicationId
                            )
                    ).first();

            if (existingApplication != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Application ID already exists.",
                        "Duplicate Application ID",
                        JOptionPane.WARNING_MESSAGE
                );

                applicationIdField.requestFocus();

                return;
            }


            // =================================================
            // STUDENT
            // =================================================

            String selectedStudent =
                    (String)
                            studentComboBox
                                    .getSelectedItem();

            if (selectedStudent == null ||
                    selectedStudent.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a student."
                );

                return;
            }

            String studentId =
                    extractId(
                            selectedStudent
                    );


            // =================================================
            // CHECK STUDENT EXISTS
            // =================================================

            Document student =
                    database.getCollection(
                            "students"
                    )
                    .find(
                            new Document(
                                    "studentId",
                                    studentId
                            )
                    )
                    .first();

            if (student == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selected student was not found.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =================================================
            // JOB DRIVE
            // =================================================

            String selectedDrive =
                    (String)
                            driveComboBox
                                    .getSelectedItem();

            if (selectedDrive == null ||
                    selectedDrive.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a job drive."
                );

                return;
            }

            String driveId =
                    extractId(
                            selectedDrive
                    );


            // =================================================
            // CHECK JOB DRIVE EXISTS
            // =================================================

            Document drive =
                    database.getCollection(
                            "job_drives"
                    )
                    .find(
                            new Document(
                                    "driveId",
                                    driveId
                            )
                    )
                    .first();

            if (drive == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selected job drive was not found.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =================================================
            // CHECK DRIVE STATUS
            // =================================================

            String driveStatus =
                    drive.getString(
                            "status"
                    );

            if (driveStatus != null &&
                    !driveStatus.equalsIgnoreCase("Open")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Applications are currently closed for this job drive.\n"
                                + "Drive Status: "
                                + driveStatus,
                        "Job Drive Closed",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =================================================
            // CHECK DUPLICATE STUDENT + DRIVE
            // =================================================

            Document duplicateApplication =
                    applications.find(
                            new Document(
                                    "studentId",
                                    studentId
                            )
                            .append(
                                    "driveId",
                                    driveId
                            )
                    )
                    .first();

            if (duplicateApplication != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "This student has already applied for this job drive.",
                        "Duplicate Application",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =================================================
            // ELIGIBILITY CHECK
            // =================================================

            String eligibilityResult =
                    EligibilityChecker.checkEligibility(
                            studentId,
                            driveId
                    );


            if (!"ELIGIBLE".equalsIgnoreCase(
                    eligibilityResult
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        eligibilityResult,
                        "Eligibility Check",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =================================================
            // APPLICATION DATE
            // =================================================

            String applicationDate =
                    dateField
                            .getText()
                            .trim();

            if (applicationDate.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter application date."
                );

                dateField.requestFocus();

                return;
            }


            // =================================================
            // VALIDATE DATE FORMAT
            // =================================================

            if (!isValidDate(applicationDate)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter date in yyyy-MM-dd format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                dateField.requestFocus();

                return;
            }


            // =================================================
            // STATUS
            // =================================================

            String status =
                    (String)
                            statusComboBox
                                    .getSelectedItem();

            if (status == null ||
                    status.trim().isEmpty()) {

                status = "Applied";
            }


            // =================================================
            // ROUNDS
            // =================================================

            List<Document> rounds =
                    new ArrayList<>();

            // No rounds have been conducted yet.
            // Rounds can be added later from
            // Update Application.


            // =================================================
            // CREATE APPLICATION DOCUMENT
            // =================================================

            Document application =
                    new Document(
                            "applicationId",
                            applicationId
                    )
                    .append(
                            "studentId",
                            studentId
                    )
                    .append(
                            "driveId",
                            driveId
                    )
                    .append(
                            "applicationDate",
                            applicationDate
                    )
                    .append(
                            "status",
                            status
                    )
                    .append(
                            "rounds",
                            rounds
                    );


            // =================================================
            // INSERT APPLICATION
            // =================================================

            applications.insertOne(
                    application
            );


            // =================================================
            // SUCCESS
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Application added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            // =================================================
            // REFRESH APPLICATION TABLE
            // =================================================

            parentPanel.refreshApplications();


            // =================================================
            // CLOSE FORM
            // =================================================

            dispose();


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error adding application:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // EXTRACT ID FROM COMBO BOX TEXT
    // =====================================================

    private String extractId(String displayText) {

        if (displayText == null) {

            return "";
        }

        int separatorIndex =
                displayText.indexOf(" - ");

        if (separatorIndex != -1) {

            return displayText
                    .substring(
                            0,
                            separatorIndex
                    )
                    .trim();
        }

        int spaceIndex =
                displayText.indexOf(" ");

        if (spaceIndex != -1) {

            return displayText
                    .substring(
                            0,
                            spaceIndex
                    )
                    .trim();
        }

        return displayText.trim();
    }


    // =====================================================
    // VALIDATE DATE
    // =====================================================

    private boolean isValidDate(
            String date
    ) {

        try {

            SimpleDateFormat format =
                    new SimpleDateFormat(
                            "yyyy-MM-dd"
                    );

            format.setLenient(false);

            format.parse(date);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}

