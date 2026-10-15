package com.placement.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class UpdateJobDriveForm extends JFrame {

    private JobDrivePanel parentPanel;
    private String driveId;

    private JTextField driveIdField;
    private JComboBox<String> companyComboBox;
    private JTextField roleField;
    private JTextField ctcField;
    private JComboBox<String> currencyComboBox;
    private JTextField eligibilityField;
    private JTextField skillsField;
    private JComboBox<String> statusComboBox;

    private MongoDatabase database;

    public UpdateJobDriveForm(JobDrivePanel parentPanel, String driveId) {

        this.parentPanel = parentPanel;
        this.driveId = driveId;

        database = MongoDBConnection.getDatabase();

        setTitle("UPDATE JOB DRIVE");
        setSize(550, 650);
        setLocationRelativeTo(parentPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createForm();

        loadCompanies();

        loadJobDrive();
    }

    private void createForm() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel titleLabel = new JLabel(
                "UPDATE JOB DRIVE",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel formPanel = new JPanel(
                new GridLayout(8, 2, 10, 15)
        );

        // -------------------------------------------------
        // DRIVE ID
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Drive ID:")
        );

        driveIdField = new JTextField();
        driveIdField.setEditable(false);

        formPanel.add(driveIdField);

        // -------------------------------------------------
        // COMPANY
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Company:")
        );

        companyComboBox =
                new JComboBox<>();

        formPanel.add(companyComboBox);

        // -------------------------------------------------
        // ROLE
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Role:")
        );

        roleField =
                new JTextField();

        formPanel.add(roleField);

        // -------------------------------------------------
        // CTC
        // -------------------------------------------------

        formPanel.add(
                new JLabel("CTC:")
        );

        ctcField =
                new JTextField();

        formPanel.add(ctcField);

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

        formPanel.add(currencyComboBox);

        // -------------------------------------------------
        // ELIGIBILITY CGPA
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Eligibility CGPA:")
        );

        eligibilityField =
                new JTextField();

        formPanel.add(eligibilityField);

        // -------------------------------------------------
        // SKILLS
        // -------------------------------------------------

        formPanel.add(
                new JLabel("Skills Required:")
        );

        skillsField =
                new JTextField();

        formPanel.add(skillsField);

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

        formPanel.add(statusComboBox);

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // BUTTON PANEL
        // -------------------------------------------------

        JPanel buttonPanel = new JPanel();

        JButton updateButton =
                new JButton("UPDATE JOB DRIVE");

        JButton cancelButton =
                new JButton("CANCEL");

        updateButton.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        cancelButton.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // -------------------------------------------------
        // BUTTON ACTIONS
        // -------------------------------------------------

        updateButton.addActionListener(
                e -> updateJobDrive()
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
                    database.getCollection("companies");

            companyComboBox.removeAllItems();

            for (Document company : companies.find()) {

                String companyId =
                        company.getString("companyId");

                String companyName =
                        company.getString("companyName");

                if (companyId != null) {

                    if (companyName != null) {

                        companyComboBox.addItem(
                                companyId + " - " + companyName
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
    // LOAD EXISTING JOB DRIVE
    // =====================================================

    private void loadJobDrive() {

        try {

            MongoCollection<Document> drives =
                    database.getCollection("job_drives");

            Document drive =
                    drives.find(
                            new Document(
                                    "driveId",
                                    driveId
                            )
                    ).first();

            if (drive == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Job Drive not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                dispose();

                return;
            }

            // -------------------------------------------------
            // DRIVE ID
            // -------------------------------------------------

            driveIdField.setText(
                    drive.getString("driveId")
            );

            // -------------------------------------------------
            // COMPANY ID
            // -------------------------------------------------

            String companyId =
                    drive.getString("companyId");

            if (companyId != null) {

                selectCompany(companyId);
            }

            // -------------------------------------------------
            // ROLE
            // -------------------------------------------------

            String role =
                    drive.getString("role");

            if (role != null) {

                roleField.setText(role);
            }

            // -------------------------------------------------
            // PACKAGE
            // -------------------------------------------------

            Object packageObject =
                    drive.get("package");

            if (packageObject instanceof Document) {

                Document packageDocument =
                        (Document) packageObject;

                Number ctcNumber =
                        packageDocument.get(
                                "ctc",
                                Number.class
                        );

                if (ctcNumber != null) {

                    ctcField.setText(
                            String.valueOf(
                                    ctcNumber.doubleValue()
                            )
                    );
                }

                String currency =
                        packageDocument.getString(
                                "currency"
                        );

                if (currency != null) {

                    currencyComboBox.setSelectedItem(
                            currency
                    );
                }

            } else if (packageObject != null) {

                ctcField.setText(
                        packageObject.toString()
                );
            }

            // -------------------------------------------------
            // ELIGIBILITY CGPA
            // -------------------------------------------------

            Number eligibilityNumber =
                    drive.get(
                            "eligibilityCGPA",
                            Number.class
                    );

            if (eligibilityNumber != null) {

                eligibilityField.setText(
                        String.valueOf(
                                eligibilityNumber.doubleValue()
                        )
                );
            }

            // -------------------------------------------------
            // SKILLS
            // -------------------------------------------------

            Object skillsObject =
                    drive.get("skillsRequired");

            if (skillsObject instanceof List<?>) {

                List<?> skills =
                        (List<?>) skillsObject;

                List<String> skillNames =
                        new ArrayList<>();

                for (Object skill : skills) {

                    if (skill != null) {

                        skillNames.add(
                                skill.toString()
                        );
                    }
                }

                skillsField.setText(
                        String.join(
                                ", ",
                                skillNames
                        )
                );
            }

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            String status =
                    drive.getString("status");

            if (status != null) {

                statusComboBox.setSelectedItem(
                        status
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading job drive:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // SELECT COMPANY IN DROPDOWN
    // =====================================================

    private void selectCompany(String companyId) {

        for (int i = 0;
             i < companyComboBox.getItemCount();
             i++) {

            String item =
                    companyComboBox.getItemAt(i);

            if (item != null &&
                    item.startsWith(companyId + " -")) {

                companyComboBox.setSelectedIndex(i);

                return;
            }

            if (item != null &&
                    item.equals(companyId)) {

                companyComboBox.setSelectedIndex(i);

                return;
            }
        }
    }

    // =====================================================
    // UPDATE JOB DRIVE
    // =====================================================

    private void updateJobDrive() {

        try {

            // -------------------------------------------------
            // GET COMPANY
            // -------------------------------------------------

            String selectedCompany =
                    (String) companyComboBox.getSelectedItem();

            if (selectedCompany == null ||
                    selectedCompany.trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a company."
                );

                return;
            }

            String companyId =
                    selectedCompany.split(" - ")[0];

            // -------------------------------------------------
            // GET ROLE
            // -------------------------------------------------

            String role =
                    roleField.getText().trim();

            if (role.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the role."
                );

                roleField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // GET CTC
            // -------------------------------------------------

            String ctcText =
                    ctcField.getText().trim();

            if (ctcText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the CTC."
                );

                ctcField.requestFocus();

                return;
            }

            double ctc;

            try {

                ctc =
                        Double.parseDouble(ctcText);

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
            // GET CURRENCY
            // -------------------------------------------------

            String currency =
                    (String) currencyComboBox.getSelectedItem();

            // -------------------------------------------------
            // GET ELIGIBILITY CGPA
            // -------------------------------------------------

            String eligibilityText =
                    eligibilityField.getText().trim();

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
                            "Eligibility CGPA must be between 0 and 10."
                    );

                    return;
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid eligibility CGPA."
                );

                eligibilityField.requestFocus();

                return;
            }

            // -------------------------------------------------
            // GET SKILLS
            // -------------------------------------------------

            String skillsText =
                    skillsField.getText().trim();

            List<String> skills =
                    new ArrayList<>();

            if (!skillsText.isEmpty()) {

                String[] skillArray =
                        skillsText.split(",");

                for (String skill : skillArray) {

                    String cleanedSkill =
                            skill.trim();

                    if (!cleanedSkill.isEmpty()) {

                        skills.add(cleanedSkill);
                    }
                }
            }

            // -------------------------------------------------
            // GET STATUS
            // -------------------------------------------------

            String status =
                    (String) statusComboBox.getSelectedItem();

            // -------------------------------------------------
            // CREATE PACKAGE DOCUMENT
            // -------------------------------------------------

            Document packageDocument =
                    new Document("ctc", ctc)
                            .append(
                                    "currency",
                                    currency
                            );

            // -------------------------------------------------
            // UPDATE DOCUMENT
            // -------------------------------------------------

            Document updateDocument =
                    new Document("$set",

                            new Document(
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
                            )
                    );

            // -------------------------------------------------
            // UPDATE MONGODB
            // -------------------------------------------------

            MongoCollection<Document> drives =
                    database.getCollection(
                            "job_drives"
                    );

            drives.updateOne(

                    new Document(
                            "driveId",
                            driveId
                    ),

                    updateDocument
            );

            // -------------------------------------------------
            // SUCCESS MESSAGE
            // -------------------------------------------------

            JOptionPane.showMessageDialog(
                    this,
                    "Job Drive updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // -------------------------------------------------
            // REFRESH PARENT TABLE
            // -------------------------------------------------

            parentPanel.refreshJobDrives();

            // -------------------------------------------------
            // CLOSE FORM
            // -------------------------------------------------

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error updating job drive:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}