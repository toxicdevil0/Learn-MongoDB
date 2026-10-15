package com.placement.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class UpdateApplicationForm extends JFrame {

    private ApplicationPanel parentPanel;
    private String applicationId;

    private JTextField applicationIdField;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> driveComboBox;
    private JTextField dateField;
    private JComboBox<String> statusComboBox;

    private JTextField aptitudeScoreField;
    private JComboBox<String> aptitudeResultComboBox;

    private JTextField technicalScoreField;
    private JComboBox<String> technicalResultComboBox;

    private MongoDatabase database;

    public UpdateApplicationForm(
            ApplicationPanel parentPanel,
            String applicationId) {

        this.parentPanel = parentPanel;
        this.applicationId = applicationId;

        database = MongoDBConnection.getDatabase();

        setTitle("UPDATE APPLICATION");

        setSize(650, 700);

        setLocationRelativeTo(parentPanel);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createForm();

        loadStudents();

        loadJobDrives();

        loadApplication();
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
                        "UPDATE APPLICATION",
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
        // FORM PANEL
        // =================================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                9,
                                2,
                                10,
                                12
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

        applicationIdField.setEditable(false);

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
        // DATE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Application Date:")
        );

        dateField =
                new JTextField();

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

        // -------------------------------------------------
        // APTITUDE RESULT
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Aptitude Result:")
        );

        aptitudeResultComboBox =
                new JComboBox<>(
                        new String[]{
                                "Pending",
                                "Passed",
                                "Failed"
                        }
                );

        formPanel.add(
                aptitudeResultComboBox
        );

        // -------------------------------------------------
        // APTITUDE SCORE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Aptitude Score:")
        );

        aptitudeScoreField =
                new JTextField();

        formPanel.add(
                aptitudeScoreField
        );

        // -------------------------------------------------
        // TECHNICAL RESULT
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Technical Result:")
        );

        technicalResultComboBox =
                new JComboBox<>(
                        new String[]{
                                "Pending",
                                "Passed",
                                "Failed"
                        }
                );

        formPanel.add(
                technicalResultComboBox
        );

        // -------------------------------------------------
        // TECHNICAL SCORE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Technical Score:")
        );

        technicalScoreField =
                new JTextField();

        formPanel.add(
                technicalScoreField
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

        JButton updateButton =
                new JButton(
                        "UPDATE APPLICATION"
                );

        JButton cancelButton =
                new JButton(
                        "CANCEL"
                );

        buttonPanel.add(
                updateButton
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

        updateButton.addActionListener(
                e -> updateApplication()
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
    // LOAD APPLICATION
    // =====================================================

    private void loadApplication() {

        try {

            MongoCollection<Document> applications =
                    database.getCollection(
                            "applications"
                    );

            Document application =
                    applications.find(
                            new Document(
                                    "applicationId",
                                    applicationId
                            )
                    ).first();

            if (application == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Application not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                dispose();

                return;
            }

            // -------------------------------------------------
            // APPLICATION ID
            // -------------------------------------------------

            applicationIdField.setText(
                    application.getString(
                            "applicationId"
                    )
            );

            // -------------------------------------------------
            // STUDENT
            // -------------------------------------------------

            String studentId =
                    application.getString(
                            "studentId"
                    );

            selectStudent(studentId);

            // -------------------------------------------------
            // DRIVE
            // -------------------------------------------------

            String driveId =
                    application.getString(
                            "driveId"
                    );

            selectDrive(driveId);

            // -------------------------------------------------
            // DATE
            // -------------------------------------------------

            Object date =
                    application.get(
                            "applicationDate"
                    );

            if (date != null) {

                dateField.setText(
                        date.toString()
                );
            }

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            String status =
                    application.getString(
                            "status"
                    );

            if (status != null) {

                statusComboBox.setSelectedItem(
                        status
                );
            }

            // -------------------------------------------------
            // ROUNDS
            // -------------------------------------------------

            Object roundsObject =
                    application.get(
                            "rounds"
                    );

            if (roundsObject instanceof List<?>) {

                List<?> rounds =
                        (List<?>) roundsObject;

                for (Object roundObject :
                        rounds) {

                    if (!(roundObject instanceof Document)) {

                        continue;
                    }

                    Document round =
                            (Document) roundObject;

                    Object roundNumber =
                            round.get("round");

                    String type =
                            round.getString(
                                    "type"
                            );

                    String result =
                            round.getString(
                                    "result"
                            );

                    Number score =
                            round.get(
                                    "score",
                                    Number.class
                            );

                    // -----------------------------------------
                    // APTITUDE
                    // -----------------------------------------

                    if (
                            type != null &&
                            type.equalsIgnoreCase(
                                    "Aptitude"
                            )
                    ) {

                        if (result != null) {

                            aptitudeResultComboBox
                                    .setSelectedItem(
                                            result
                                    );
                        }

                        if (score != null) {

                            aptitudeScoreField.setText(
                                    String.valueOf(
                                            score.doubleValue()
                                    )
                            );
                        }
                    }

                    // -----------------------------------------
                    // TECHNICAL
                    // -----------------------------------------

                    if (
                            type != null &&
                            type.equalsIgnoreCase(
                                    "Technical"
                            )
                    ) {

                        if (result != null) {

                            technicalResultComboBox
                                    .setSelectedItem(
                                            result
                                    );
                        }

                        if (score != null) {

                            technicalScoreField.setText(
                                    String.valueOf(
                                            score.doubleValue()
                                    )
                            );
                        }
                    }
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading application:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // SELECT STUDENT
    // =====================================================

    private void selectStudent(
            String studentId
    ) {

        if (studentId == null) {

            return;
        }

        for (int i = 0;
             i < studentComboBox.getItemCount();
             i++) {

            String item =
                    studentComboBox.getItemAt(i);

            if (
                    item.equals(studentId) ||
                    item.startsWith(
                            studentId + " -"
                    )
            ) {

                studentComboBox.setSelectedIndex(i);

                return;
            }
        }
    }

    // =====================================================
    // SELECT DRIVE
    // =====================================================

    private void selectDrive(
            String driveId
    ) {

        if (driveId == null) {

            return;
        }

        for (int i = 0;
             i < driveComboBox.getItemCount();
             i++) {

            String item =
                    driveComboBox.getItemAt(i);

            if (
                    item.equals(driveId) ||
                    item.startsWith(
                            driveId + " -"
                    )
            ) {

                driveComboBox.setSelectedIndex(i);

                return;
            }
        }
    }

    // =====================================================
    // UPDATE APPLICATION
    // =====================================================

    private void updateApplication() {

        try {

            // -------------------------------------------------
            // STUDENT
            // -------------------------------------------------

            String selectedStudent =
                    (String)
                            studentComboBox
                                    .getSelectedItem();

            if (
                    selectedStudent == null ||
                    selectedStudent.trim().isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a student."
                );

                return;
            }

            String studentId =
                    selectedStudent
                            .split(" - ")[0];

            // -------------------------------------------------
            // DRIVE
            // -------------------------------------------------

            String selectedDrive =
                    (String)
                            driveComboBox
                                    .getSelectedItem();

            if (
                    selectedDrive == null ||
                    selectedDrive.trim().isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a job drive."
                );

                return;
            }

            String driveId =
                    selectedDrive
                            .split(" - ")[0];

            // -------------------------------------------------
            // DATE
            // -------------------------------------------------

            String applicationDate =
                    dateField
                            .getText()
                            .trim();

            if (applicationDate.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter application date."
                );

                return;
            }

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            String status =
                    (String)
                            statusComboBox
                                    .getSelectedItem();

            // -------------------------------------------------
            // ROUNDS
            // -------------------------------------------------

            List<Document> rounds =
                    new ArrayList<>();

            // =================================================
            // APTITUDE ROUND
            // =================================================

            String aptitudeResult =
                    (String)
                            aptitudeResultComboBox
                                    .getSelectedItem();

            String aptitudeScoreText =
                    aptitudeScoreField
                            .getText()
                            .trim();

            Document aptitudeRound =
                    new Document(
                            "round",
                            1
                    )
                    .append(
                            "type",
                            "Aptitude"
                    )
                    .append(
                            "result",
                            aptitudeResult
                    );

            if (!aptitudeScoreText.isEmpty()) {

                try {

                    double score =
                            Double.parseDouble(
                                    aptitudeScoreText
                            );

                    aptitudeRound.append(
                            "score",
                            score
                    );

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Aptitude score."
                    );

                    return;
                }
            }

            rounds.add(
                    aptitudeRound
            );

            // =================================================
            // TECHNICAL ROUND
            // =================================================

            String technicalResult =
                    (String)
                            technicalResultComboBox
                                    .getSelectedItem();

            String technicalScoreText =
                    technicalScoreField
                            .getText()
                            .trim();

            Document technicalRound =
                    new Document(
                            "round",
                            2
                    )
                    .append(
                            "type",
                            "Technical"
                    )
                    .append(
                            "result",
                            technicalResult
                    );

            if (!technicalScoreText.isEmpty()) {

                try {

                    double score =
                            Double.parseDouble(
                                    technicalScoreText
                            );

                    technicalRound.append(
                            "score",
                            score
                    );

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Technical score."
                    );

                    return;
                }
            }

            rounds.add(
                    technicalRound
            );

            // =================================================
            // UPDATE DOCUMENT
            // =================================================

            Document updateDocument =
                    new Document(
                            "$set",
                            new Document(
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
                            )
                    );

            // =================================================
            // UPDATE MONGODB
            // =================================================

            MongoCollection<Document> applications =
                    database.getCollection(
                            "applications"
                    );

            applications.updateOne(

                    new Document(
                            "applicationId",
                            applicationId
                    ),

                    updateDocument
            );

            // =================================================
            // SUCCESS
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Application updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // =================================================
            // REFRESH PARENT
            // =================================================

            parentPanel.refreshApplications();

            // =================================================
            // CLOSE
            // =================================================

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error updating application:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}