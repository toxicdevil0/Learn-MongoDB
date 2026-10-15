
package com.placement.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class PlacementAnalytics extends JFrame {

    private MongoDatabase database;

    // =====================================================
    // STAT LABELS
    // =====================================================

    private JLabel totalStudentsLabel;
    private JLabel totalCompaniesLabel;
    private JLabel totalDrivesLabel;
    private JLabel totalApplicationsLabel;

    private JLabel selectedStudentsLabel;
    private JLabel placementPercentageLabel;
    private JLabel averagePackageLabel;
    private JLabel highestPackageLabel;

    // =====================================================
    // TEXT AREAS
    // =====================================================

    private JTextArea statusArea;
    private JTextArea departmentArea;
    private JTextArea companyArea;
    private JTextArea multipleApplicationsArea;
    private JTextArea unplacedStudentsArea;
    private JTextArea placementDetailsArea;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PlacementAnalytics() {

        database =
                MongoDBConnection.getDatabase();

        setTitle("PLACEMENT ANALYTICS");

        setSize(1250, 800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadAnalytics();
    }


    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        // =================================================
        // TITLE
        // =================================================

        JLabel titleLabel =
                new JLabel(
                        "PLACEMENT ANALYTICS",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );


        // =================================================
        // TOP STAT CARDS
        // =================================================

        JPanel statsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                4,
                                10,
                                10
                        )
                );

        totalStudentsLabel =
                createStatCard(
                        statsPanel,
                        "TOTAL STUDENTS"
                );

        totalCompaniesLabel =
                createStatCard(
                        statsPanel,
                        "TOTAL COMPANIES"
                );

        totalDrivesLabel =
                createStatCard(
                        statsPanel,
                        "TOTAL JOB DRIVES"
                );

        totalApplicationsLabel =
                createStatCard(
                        statsPanel,
                        "TOTAL APPLICATIONS"
                );

        selectedStudentsLabel =
                createStatCard(
                        statsPanel,
                        "SELECTED STUDENTS"
                );

        placementPercentageLabel =
                createStatCard(
                        statsPanel,
                        "PLACEMENT %"
                );

        averagePackageLabel =
                createStatCard(
                        statsPanel,
                        "AVERAGE PACKAGE"
                );

        highestPackageLabel =
                createStatCard(
                        statsPanel,
                        "HIGHEST PACKAGE"
                );


        mainPanel.add(
                statsPanel,
                BorderLayout.CENTER
        );


        // =================================================
        // ANALYTICS AREA
        // =================================================

        JPanel analyticsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                10,
                                10
                        )
                );


        // -------------------------------------------------
        // APPLICATION STATUS
        // -------------------------------------------------

        statusArea =
                createTextArea(
                        analyticsPanel,
                        "APPLICATION STATUS"
                );


        // -------------------------------------------------
        // DEPARTMENT-WISE PLACEMENTS
        // -------------------------------------------------

        departmentArea =
                createTextArea(
                        analyticsPanel,
                        "DEPARTMENT-WISE PLACEMENTS"
                );


        // -------------------------------------------------
        // COMPANY-WISE PLACEMENTS
        // -------------------------------------------------

        companyArea =
                createTextArea(
                        analyticsPanel,
                        "COMPANY-WISE PLACEMENTS"
                );


        // -------------------------------------------------
        // MULTIPLE APPLICATIONS
        // -------------------------------------------------

        multipleApplicationsArea =
                createTextArea(
                        analyticsPanel,
                        "MULTIPLE APPLICATIONS"
                );


        // -------------------------------------------------
        // UNPLACED STUDENTS
        // -------------------------------------------------

        unplacedStudentsArea =
                createTextArea(
                        analyticsPanel,
                        "UNPLACED STUDENTS"
                );


        // -------------------------------------------------
        // PLACEMENT DETAILS
        // -------------------------------------------------

        placementDetailsArea =
                createTextArea(
                        analyticsPanel,
                        "PLACEMENT DETAILS"
                );


        mainPanel.add(
                analyticsPanel,
                BorderLayout.SOUTH
        );


        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttonPanel =
                new JPanel();

        JButton refreshButton =
                new JButton(
                        "REFRESH ANALYTICS"
                );

        JButton backButton =
                new JButton(
                        "BACK"
                );


        refreshButton.addActionListener(
                e -> refreshAnalytics()
        );

        backButton.addActionListener(
                e -> dispose()
        );


        buttonPanel.add(
                refreshButton
        );

        buttonPanel.add(
                backButton
        );


        mainPanel.add(
                buttonPanel,
                BorderLayout.PAGE_END
        );


        add(mainPanel);
    }


    // =====================================================
    // CREATE STAT CARD
    // =====================================================

    private JLabel createStatCard(
            JPanel parent,
            String title
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBorder(
                BorderFactory.createLineBorder(
                        Color.GRAY,
                        1
                )
        );


        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        JLabel valueLabel =
                new JLabel(
                        "0",
                        SwingConstants.CENTER
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );


        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );


        parent.add(card);


        return valueLabel;
    }


    // =====================================================
    // CREATE TEXT AREA
    // =====================================================

    private JTextArea createTextArea(
            JPanel parent,
            String title
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );


        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        JTextArea area =
                new JTextArea();

        area.setEditable(false);

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(area);


        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        parent.add(panel);


        return area;
    }


    // =====================================================
    // LOAD ALL ANALYTICS
    // =====================================================

    private void loadAnalytics() {

        try {

            loadBasicCounts();

            loadPlacementStatistics();

            loadApplicationStatus();

            loadDepartmentPlacements();

            loadCompanyPlacements();

            loadMultipleApplications();

            loadUnplacedStudents();

            loadPlacementDetails();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading analytics:\n"
                            + e.getMessage(),
                    "Analytics Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // BASIC COUNTS
    // =====================================================

    private void loadBasicCounts() {

        long students =
                database
                        .getCollection("students")
                        .countDocuments();


        long companies =
                database
                        .getCollection("companies")
                        .countDocuments();


        long drives =
                database
                        .getCollection("job_drives")
                        .countDocuments();


        long applications =
                database
                        .getCollection("applications")
                        .countDocuments();


        totalStudentsLabel.setText(
                String.valueOf(students)
        );

        totalCompaniesLabel.setText(
                String.valueOf(companies)
        );

        totalDrivesLabel.setText(
                String.valueOf(drives)
        );

        totalApplicationsLabel.setText(
                String.valueOf(applications)
        );
    }


    // =====================================================
    // PLACEMENT STATISTICS
    // =====================================================

    private void loadPlacementStatistics() {

        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        // =================================================
        // SELECTED STUDENTS
        // =================================================

        long selectedStudents =
                applications
                        .aggregate(
                                Arrays.asList(

                                        new Document(
                                                "$match",
                                                new Document(
                                                        "status",
                                                        "Selected"
                                                )
                                        ),

                                        new Document(
                                                "$group",
                                                new Document(
                                                        "_id",
                                                        "$studentId"
                                                )
                                        ),

                                        new Document(
                                                "$count",
                                                "total"
                                                )
                                )
                        )
                        .first() != null
                                ?
                                applications
                                        .aggregate(
                                                Arrays.asList(

                                                        new Document(
                                                                "$match",
                                                                new Document(
                                                                        "status",
                                                                        "Selected"
                                                                )
                                                        ),

                                                        new Document(
                                                                "$group",
                                                                new Document(
                                                                        "_id",
                                                                        "$studentId"
                                                                )
                                                        )
                                                )
                                        )
                                        .into(
                                                new java.util.ArrayList<>()
                                        )
                                        .size()
                                : 0;


        selectedStudentsLabel.setText(
                String.valueOf(
                        selectedStudents
                )
        );


        // =================================================
        // PLACEMENT PERCENTAGE
        // =================================================

        long totalStudents =
                database
                        .getCollection("students")
                        .countDocuments();


        double placementPercentage = 0;


        if (totalStudents > 0) {

            placementPercentage =
                    (
                            selectedStudents
                                    * 100.0
                    )
                    / totalStudents;
        }


        placementPercentageLabel.setText(
                String.format(
                        "%.2f%%",
                        placementPercentage
                )
        );


        // =================================================
        // PACKAGE STATISTICS
        // =================================================

        AggregateIterable<Document> packageResult =
                applications.aggregate(
                        Arrays.asList(

                                new Document(
                                        "$match",
                                        new Document(
                                                "status",
                                                "Selected"
                                        )
                                ),

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "job_drives"
                                        )
                                        .append(
                                                "localField",
                                                "driveId"
                                        )
                                        .append(
                                                "foreignField",
                                                "driveId"
                                        )
                                        .append(
                                                "as",
                                                "drive"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$drive"
                                ),

                                new Document(
                                        "$group",
                                        new Document(
                                                "_id",
                                                null
                                        )
                                        .append(
                                                "averagePackage",
                                                new Document(
                                                        "$avg",
                                                        "$drive.package.ctc"
                                                )
                                        )
                                        .append(
                                                "highestPackage",
                                                new Document(
                                                        "$max",
                                                        "$drive.package.ctc"
                                                )
                                        )
                                )
                        )
                );


        Document packageDocument =
                packageResult.first();


        if (packageDocument != null) {

            Number average =
                    packageDocument.get(
                            "averagePackage",
                            Number.class
                    );

            Number highest =
                    packageDocument.get(
                            "highestPackage",
                            Number.class
                    );


            if (average != null) {

                averagePackageLabel.setText(
                        String.format(
                                "%.2f LPA",
                                average.doubleValue()
                        )
                );

            } else {

                averagePackageLabel.setText(
                        "0 LPA"
                );
            }


            if (highest != null) {

                highestPackageLabel.setText(
                        String.format(
                                "%.2f LPA",
                                highest.doubleValue()
                        )
                );

            } else {

                highestPackageLabel.setText(
                        "0 LPA"
                );
            }

        } else {

            averagePackageLabel.setText(
                    "0 LPA"
            );

            highestPackageLabel.setText(
                    "0 LPA"
            );
        }
    }


    // =====================================================
    // APPLICATION STATUS
    // =====================================================

    private void loadApplicationStatus() {

        statusArea.setText("");


        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        AggregateIterable<Document> result =
                applications.aggregate(
                        Arrays.asList(

                                new Document(
                                        "$group",
                                        new Document(
                                                "_id",
                                                "$status"
                                        )
                                        .append(
                                                "count",
                                                new Document(
                                                        "$sum",
                                                        1
                                                )
                                        )
                                ),

                                new Document(
                                        "$sort",
                                        new Document(
                                                "count",
                                                -1
                                        )
                                )
                        )
                );


        for (Document document :
                result) {

            String status =
                    document.getString(
                            "_id"
                    );

            Number count =
                    document.get(
                            "count",
                            Number.class
                    );


            statusArea.append(
                    String.format(
                            "%-20s : %d%n",
                            status,
                            count != null
                                    ?
                                    count.intValue()
                                    :
                                    0
                    )
            );
        }


        if (statusArea.getText().isEmpty()) {

            statusArea.setText(
                    "No application data available."
            );
        }
    }


    // =====================================================
    // DEPARTMENT-WISE PLACEMENTS
    // =====================================================

    private void loadDepartmentPlacements() {

        departmentArea.setText("");


        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        AggregateIterable<Document> result =
                applications.aggregate(
                        Arrays.asList(

                                new Document(
                                        "$match",
                                        new Document(
                                                "status",
                                                "Selected"
                                        )
                                ),

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "students"
                                        )
                                        .append(
                                                "localField",
                                                "studentId"
                                        )
                                        .append(
                                                "foreignField",
                                                "studentId"
                                        )
                                        .append(
                                                "as",
                                                "student"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$student"
                                ),

                                new Document(
                                        "$group",
                                        new Document(
                                                "_id",
                                                "$student.department"
                                        )
                                        .append(
                                                "students",
                                                new Document(
                                                        "$addToSet",
                                                        "$studentId"
                                                )
                                        )
                                ),

                                new Document(
                                        "$project",
                                        new Document(
                                                "_id",
                                                0
                                        )
                                        .append(
                                                "department",
                                                "$_id"
                                        )
                                        .append(
                                                "count",
                                                new Document(
                                                        "$size",
                                                        "$students"
                                                )
                                        )
                                ),

                                new Document(
                                        "$sort",
                                        new Document(
                                                "count",
                                                -1
                                        )
                                )
                        )
                );


        for (Document document :
                result) {

            String department =
                    document.getString(
                            "department"
                    );

            Number count =
                    document.get(
                            "count",
                            Number.class
                    );


            departmentArea.append(
                    String.format(
                            "%-15s : %d placed%n",
                            department,
                            count != null
                                    ?
                                    count.intValue()
                                    :
                                    0
                    )
            );
        }


        if (departmentArea.getText().isEmpty()) {

            departmentArea.setText(
                    "No placement data available."
            );
        }
    }


    // =====================================================
    // COMPANY-WISE PLACEMENTS
    // =====================================================

    private void loadCompanyPlacements() {

        companyArea.setText("");


        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        AggregateIterable<Document> result =
                applications.aggregate(
                        Arrays.asList(

                                new Document(
                                        "$match",
                                        new Document(
                                                "status",
                                                "Selected"
                                        )
                                ),

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "job_drives"
                                        )
                                        .append(
                                                "localField",
                                                "driveId"
                                        )
                                        .append(
                                                "foreignField",
                                                "driveId"
                                        )
                                        .append(
                                                "as",
                                                "drive"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$drive"
                                ),

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "companies"
                                        )
                                        .append(
                                                "localField",
                                                "drive.companyId"
                                        )
                                        .append(
                                                "foreignField",
                                                "companyId"
                                        )
                                        .append(
                                                "as",
                                                "company"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$company"
                                ),

                                new Document(
                                        "$group",
                                        new Document(
                                                "_id",
                                                "$company.companyId"
                                        )
                                        .append(
                                                "companyName",
                                                new Document(
                                                        "$first",
                                                        "$company.companyName"
                                                )
                                        )
                                        .append(
                                                "students",
                                                new Document(
                                                        "$addToSet",
                                                        "$studentId"
                                                )
                                        )
                                ),

                                new Document(
                                        "$project",
                                        new Document(
                                                "_id",
                                                0
                                        )
                                        .append(
                                                "companyId",
                                                "$_id"
                                        )
                                        .append(
                                                "companyName",
                                                1
                                        )
                                        .append(
                                                "count",
                                                new Document(
                                                        "$size",
                                                        "$students"
                                                )
                                        )
                                ),

                                new Document(
                                        "$sort",
                                        new Document(
                                                "count",
                                                -1
                                        )
                                )
                        )
                );


        for (Document document :
                result) {

            String companyId =
                    document.getString(
                            "companyId"
                    );

            String companyName =
                    document.getString(
                            "companyName"
                    );

            Number count =
                    document.get(
                            "count",
                            Number.class
                    );


            companyArea.append(
                    String.format(
                            "%s - %s : %d placed%n",
                            companyId,
                            companyName != null
                                    ?
                                    companyName
                                    :
                                    "",
                            count != null
                                    ?
                                    count.intValue()
                                    :
                                    0
                    )
            );
        }


        if (companyArea.getText().isEmpty()) {

            companyArea.setText(
                    "No company placement data available."
            );
        }
    }


    // =====================================================
    // MULTIPLE APPLICATIONS
    // =====================================================

    private void loadMultipleApplications() {

        multipleApplicationsArea.setText("");


        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        AggregateIterable<Document> result =
                applications.aggregate(
                        Arrays.asList(

                                new Document(
                                        "$group",
                                        new Document(
                                                "_id",
                                                "$studentId"
                                        )
                                        .append(
                                                "applicationCount",
                                                new Document(
                                                        "$sum",
                                                        1
                                                )
                                        )
                                ),

                                new Document(
                                        "$match",
                                        new Document(
                                                "applicationCount",
                                                new Document(
                                                        "$gt",
                                                        1
                                                )
                                        )
                                ),

                                new Document(
                                        "$sort",
                                        new Document(
                                                "applicationCount",
                                                -1
                                        )
                                )
                        )
                );


        for (Document document :
                result) {

            String studentId =
                    document.getString(
                            "_id"
                    );

            Number count =
                    document.get(
                            "applicationCount",
                            Number.class
                    );


            multipleApplicationsArea.append(
                    String.format(
                            "%-12s : %d applications%n",
                            studentId,
                            count != null
                                    ?
                                    count.intValue()
                                    :
                                    0
                    )
            );
        }


        if (multipleApplicationsArea
                .getText()
                .isEmpty()) {

            multipleApplicationsArea.setText(
                    "No students with multiple applications."
            );
        }
    }


    // =====================================================
    // UNPLACED STUDENTS
    // =====================================================

    private void loadUnplacedStudents() {

        unplacedStudentsArea.setText("");


        MongoCollection<Document> students =
                database.getCollection(
                        "students"
                );


        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        List<String> selectedStudentIds =
                applications
                        .distinct(
                                "studentId",
                                new Document(
                                        "status",
                                        "Selected"
                                ),
                                String.class
                        )
                        .into(
                                new java.util.ArrayList<>()
                        );


        Document filter;


        if (selectedStudentIds.isEmpty()) {

            filter =
                    new Document();

        } else {

            filter =
                    new Document(
                            "studentId",
                            new Document(
                                    "$nin",
                                    selectedStudentIds
                            )
                    );
        }


        for (Document student :
                students.find(filter)) {

            String studentId =
                    student.getString(
                            "studentId"
                    );

            String name =
                    student.getString(
                            "name"
                    );

            String department =
                    student.getString(
                            "department"
                    );


            unplacedStudentsArea.append(
                    String.format(
                            "%-10s %-20s %-12s%n",
                            studentId != null
                                    ?
                                    studentId
                                    :
                                    "",
                            name != null
                                    ?
                                    name
                                    :
                                    "",
                            department != null
                                    ?
                                    department
                                    :
                                    ""
                    )
            );
        }


        if (unplacedStudentsArea
                .getText()
                .isEmpty()) {

            unplacedStudentsArea.setText(
                    "All students are placed."
            );
        }
    }


    // =====================================================
    // PLACEMENT DETAILS
    // =====================================================

    private void loadPlacementDetails() {

        placementDetailsArea.setText("");


        MongoCollection<Document> applications =
                database.getCollection(
                        "applications"
                );


        AggregateIterable<Document> result =
                applications.aggregate(
                        Arrays.asList(

                                new Document(
                                        "$match",
                                        new Document(
                                                "status",
                                                "Selected"
                                        )
                                ),

                                // ---------------------------------
                                // APPLICATION → STUDENT
                                // ---------------------------------

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "students"
                                        )
                                        .append(
                                                "localField",
                                                "studentId"
                                        )
                                        .append(
                                                "foreignField",
                                                "studentId"
                                        )
                                        .append(
                                                "as",
                                                "student"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$student"
                                ),


                                // ---------------------------------
                                // APPLICATION → JOB DRIVE
                                // ---------------------------------

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "job_drives"
                                        )
                                        .append(
                                                "localField",
                                                "driveId"
                                        )
                                        .append(
                                                "foreignField",
                                                "driveId"
                                        )
                                        .append(
                                                "as",
                                                "drive"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$drive"
                                ),


                                // ---------------------------------
                                // JOB DRIVE → COMPANY
                                // ---------------------------------

                                new Document(
                                        "$lookup",
                                        new Document(
                                                "from",
                                                "companies"
                                        )
                                        .append(
                                                "localField",
                                                "drive.companyId"
                                        )
                                        .append(
                                                "foreignField",
                                                "companyId"
                                        )
                                        .append(
                                                "as",
                                                "company"
                                        )
                                ),

                                new Document(
                                        "$unwind",
                                        "$company"
                                ),


                                // ---------------------------------
                                // PROJECT REQUIRED FIELDS
                                // ---------------------------------

                                new Document(
                                        "$project",
                                        new Document(
                                                "_id",
                                                0
                                        )
                                        .append(
                                                "studentId",
                                                1
                                        )
                                        .append(
                                                "studentName",
                                                "$student.name"
                                        )
                                        .append(
                                                "department",
                                                "$student.department"
                                        )
                                        .append(
                                                "role",
                                                "$drive.role"
                                        )
                                        .append(
                                                "companyName",
                                                "$company.companyName"
                                        )
                                        .append(
                                                "ctc",
                                                "$drive.package.ctc"
                                        )
                                ),

                                new Document(
                                        "$sort",
                                        new Document(
                                                "studentId",
                                                1
                                        )
                                )
                        )
                );


        for (Document document :
                result) {

            String studentId =
                    document.getString(
                            "studentId"
                    );

            String studentName =
                    document.getString(
                            "studentName"
                    );

            String department =
                    document.getString(
                            "department"
                    );

            String role =
                    document.getString(
                            "role"
                    );

            String companyName =
                    document.getString(
                            "companyName"
                    );

            Number ctc =
                    document.get(
                            "ctc",
                            Number.class
                    );


            placementDetailsArea.append(
                    String.format(
                            "%s | %s | %s | %s | %s | %.2f LPA%n",
                            studentId != null
                                    ?
                                    studentId
                                    :
                                    "",
                            studentName != null
                                    ?
                                    studentName
                                    :
                                    "",
                            department != null
                                    ?
                                    department
                                    :
                                    "",
                            companyName != null
                                    ?
                                    companyName
                                    :
                                    "",
                            role != null
                                    ?
                                    role
                                    :
                                    "",
                            ctc != null
                                    ?
                                    ctc.doubleValue()
                                    :
                                    0.0
                    )
            );
        }


        if (placementDetailsArea
                .getText()
                .isEmpty()) {

            placementDetailsArea.setText(
                    "No selected students yet."
            );
        }
    }


    // =====================================================
    // REFRESH
    // =====================================================

    public void refreshAnalytics() {

        loadAnalytics();
    }
}

