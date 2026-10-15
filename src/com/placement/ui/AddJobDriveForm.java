package com.placement.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class AddJobDriveForm extends JFrame {

    private JobDrivePanel parentPanel;

    private JTextField driveIdField;
    private JComboBox<String> companyComboBox;
    private JTextField roleField;
    private JTextField ctcField;
    private JComboBox<String> currencyComboBox;
    private JTextField eligibilityField;
    private JTextField skillsField;
    private JComboBox<String> statusComboBox;

    private MongoDatabase database;

    public AddJobDriveForm(JobDrivePanel parentPanel) {

        this.parentPanel = parentPanel;

        database = MongoDBConnection.getDatabase();

        setTitle("ADD JOB DRIVE");

        setSize(550, 650);

        setLocationRelativeTo(parentPanel);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createForm();

        loadCompanies();
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
                        "ADD JOB DRIVE",
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
                                8,
                                2,
                                10,
                                15
                        )
                );

        // -------------------------------------------------
        // DRIVE ID
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Drive ID:")
        );

        driveIdField =
                new JTextField();

        formPanel.add(
                driveIdField
        );

        // -------------------------------------------------
        // COMPANY
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Company:")
        );

        companyComboBox =
                new JComboBox<>();

        formPanel.add(
                companyComboBox
        );

        // -------------------------------------------------
        // ROLE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Role:")
        );

        roleField =
                new JTextField();

        formPanel.add(
                roleField
        );

        // -------------------------------------------------
        // CTC
        // -------------------------------------------------

        formPanel.add(
                new JLabel("CTC:")
        );

        ctcField =
                new JTextField();

        formPanel.add(
                ctcField
        );

        // -------------------------------------------------
        // CURRENCY
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Currency:")
        );

        currencyComboBox =
                new JComboBox<>(
                        new String[]{
                                "LPA",
                                "KPA",
                                "INR"
                        }
                );

        formPanel.add(
                currencyComboBox
        );

        // -------------------------------------------------
        // ELIGIBILITY CGPA
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Eligibility CGPA:")
        );

        eligibilityField =
                new JTextField();

        formPanel.add(
                eligibilityField
        );

        // -------------------------------------------------
        // SKILLS
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Skills Required:")
        );

        skillsField =
                new JTextField();

        formPanel.add(
                skillsField
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
                                "Open",
                                "Closed",
                                "Upcoming"
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
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel();

        JButton addButton =
                new JButton(
                        "ADD JOB DRIVE"
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
        // BUTTON EVENTS
        // =================================================

        addButton.addActionListener(
                e -> addJobDrive()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );

        add(mainPanel);
    }

    // =====================================================
    // LOAD COMPANIES
    // =====================================================

    private void loadCompanies() {

        try {

            MongoCollection<Document> companies =
                    database.getCollection(
                            "companies"
                    );

            companyComboBox.removeAllItems();

            for (Document company :
                    companies.find()) {

                String companyId =
                        company.getString(
                                "companyId"
                        );

                String companyName =
                        company.getString(
                                "companyName"
                        );

                if (companyId != null) {

                    if (companyName != null) {

                        companyComboBox.addItem(
                                companyId
                                        + " - "
                                        + companyName
                        );

                    } else {

                        companyComboBox.addItem(
                                companyId
                        );
                    }
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading companies:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // ADD JOB DRIVE
    // =====================================================

    private void addJobDrive() {

        try {

            // -------------------------------------------------
            // DRIVE ID
            // -------------------------------------------------

            String driveId =
                    driveIdField
                            .getText()
                            .trim();

            if (driveId.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter Drive ID."
                );

                driveIdField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // CHECK DUPLICATE DRIVE ID
            // -------------------------------------------------

            MongoCollection<Document> drives =
                    database.getCollection(
                            "job_drives"
                    );

            Document existingDrive =
                    drives.find(
                            new Document(
                                    "driveId",
                                    driveId
                            )
                    ).first();

            if (existingDrive != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Drive ID already exists."
                );

                driveIdField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // COMPANY
            // -------------------------------------------------

            String selectedCompany =
                    (String)
                            companyComboBox
                                    .getSelectedItem();

            if (selectedCompany == null ||
                    selectedCompany.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a company."
                );

                return;
            }

            String companyId;

            if (selectedCompany.contains(" - ")) {

                companyId =
                        selectedCompany
                                .split(" - ")[0];

            } else {

                companyId =
                        selectedCompany;
            }

            // -------------------------------------------------
            // ROLE
            // -------------------------------------------------

            String role =
                    roleField
                            .getText()
                            .trim();

            if (role.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the role."
                );

                roleField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // CTC
            // -------------------------------------------------

            String ctcText =
                    ctcField
                            .getText()
                            .trim();

            if (ctcText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter CTC."
                );

                ctcField.requestFocus();

                return;
            }

            double ctc;

            try {

                ctc =
                        Double.parseDouble(
                                ctcText
                        );

                if (ctc <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "CTC must be greater than 0."
                    );

                    return;
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid CTC."
                );

                ctcField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // CURRENCY
            // -------------------------------------------------

            String currency =
                    (String)
                            currencyComboBox
                                    .getSelectedItem();

            // -------------------------------------------------
            // ELIGIBILITY CGPA
            // -------------------------------------------------

            String eligibilityText =
                    eligibilityField
                            .getText()
                            .trim();

            if (eligibilityText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter eligibility CGPA."
                );

                eligibilityField.requestFocus();

                return;
            }

            double eligibilityCGPA;

            try {

                eligibilityCGPA =
                        Double.parseDouble(
                                eligibilityText
                        );

                if (eligibilityCGPA < 0 ||
                        eligibilityCGPA > 10) {

                    JOptionPane.showMessageDialog(
                            this,
                            "CGPA must be between 0 and 10."
                    );

                    return;
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid CGPA."
                );

                eligibilityField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // SKILLS
            // -------------------------------------------------

            String skillsText =
                    skillsField
                            .getText()
                            .trim();

            List<String> skills =
                    new ArrayList<>();

            if (!skillsText.isEmpty()) {

                String[] skillArray =
                        skillsText.split(",");

                for (String skill :
                        skillArray) {

                    String cleanedSkill =
                            skill.trim();

                    if (!cleanedSkill.isEmpty()) {

                        skills.add(
                                cleanedSkill
                        );
                    }
                }
            }

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            String status =
                    (String)
                            statusComboBox
                                    .getSelectedItem();

            // -------------------------------------------------
            // PACKAGE DOCUMENT
            // -------------------------------------------------

            Document packageDocument =
                    new Document(
                            "ctc",
                            ctc
                    )
                    .append(
                            "currency",
                            currency
                    );

            // -------------------------------------------------
            // JOB DRIVE DOCUMENT
            // -------------------------------------------------

            Document jobDrive =
                    new Document(
                            "driveId",
                            driveId
                    )
                    .append(
                            "companyId",
                            companyId
                    )
                    .append(
                            "role",
                            role
                    )
                    .append(
                            "package",
                            packageDocument
                    )
                    .append(
                            "eligibilityCGPA",
                            eligibilityCGPA
                    )
                    .append(
                            "skillsRequired",
                            skills
                    )
                    .append(
                            "status",
                            status
                    );

            // -------------------------------------------------
            // INSERT
            // -------------------------------------------------

            drives.insertOne(
                    jobDrive
            );

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            JOptionPane.showMessageDialog(
                    this,
                    "Job Drive added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // -------------------------------------------------
            // REFRESH PARENT TABLE
            // -------------------------------------------------

            parentPanel.refreshJobDrives();

            // -------------------------------------------------
            // CLOSE
            // -------------------------------------------------

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error adding job drive:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}