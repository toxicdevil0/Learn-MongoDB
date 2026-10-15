
package com.placement.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class AddCompanyForm extends JFrame {

    private CompanyPanel parent;

    private JTextField companyIdField;
    private JTextField companyNameField;
    private JTextField industryField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField rolesField;
    private JTextField minimumCGPAField;
    private JTextField departmentsField;


    // ============================================
    // CONSTRUCTOR
    // ============================================

    public AddCompanyForm(
            CompanyPanel parent
    ) {

        this.parent = parent;

        setTitle(
                "Add Company"
        );

        setSize(
                550,
                500
        );

        setLocationRelativeTo(
                parent
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createForm();
    }


    // ============================================
    // CREATE FORM
    // ============================================

    private void createForm() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                8,
                                2,
                                10,
                                10
                        )
                );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );


        // ========================================
        // FIELDS
        // ========================================

        companyIdField =
                new JTextField();

        companyNameField =
                new JTextField();

        industryField =
                new JTextField();

        cityField =
                new JTextField();

        stateField =
                new JTextField();

        rolesField =
                new JTextField();

        minimumCGPAField =
                new JTextField();

        departmentsField =
                new JTextField();


        // ========================================
        // ADD FIELDS
        // ========================================

        panel.add(
                new JLabel("Company ID:")
        );

        panel.add(
                companyIdField
        );


        panel.add(
                new JLabel("Company Name:")
        );

        panel.add(
                companyNameField
        );


        panel.add(
                new JLabel("Industry:")
        );

        panel.add(
                industryField
        );


        panel.add(
                new JLabel("City:")
        );

        panel.add(
                cityField
        );


        panel.add(
                new JLabel("State:")
        );

        panel.add(
                stateField
        );


        panel.add(
                new JLabel("Hiring Roles:")
        );

        panel.add(
                rolesField
        );


        panel.add(
                new JLabel("Minimum CGPA:")
        );

        panel.add(
                minimumCGPAField
        );


        panel.add(
                new JLabel("Allowed Departments:")
        );

        panel.add(
                departmentsField
        );


        JButton saveButton =
                new JButton(
                        "SAVE COMPANY"
                );


        JButton cancelButton =
                new JButton(
                        "CANCEL"
                );


        panel.add(
                saveButton
        );

        panel.add(
                cancelButton
        );


        add(panel);


        // ========================================
        // BUTTON EVENTS
        // ========================================

        saveButton.addActionListener(
                e -> saveCompany()
        );


        cancelButton.addActionListener(
                e -> dispose()
        );
    }


    // ============================================
    // SAVE COMPANY
    // ============================================

    private void saveCompany() {

        try {

            // ========================================
            // GET VALUES
            // ========================================

            String companyId =
                    companyIdField
                    .getText()
                    .trim();


            String companyName =
                    companyNameField
                    .getText()
                    .trim();


            String industry =
                    industryField
                    .getText()
                    .trim();


            String city =
                    cityField
                    .getText()
                    .trim();


            String state =
                    stateField
                    .getText()
                    .trim();


            String rolesText =
                    rolesField
                    .getText()
                    .trim();


            double minimumCGPA =
                    Double.parseDouble(
                            minimumCGPAField
                            .getText()
                            .trim()
                    );


            String departmentsText =
                    departmentsField
                    .getText()
                    .trim();


            // ========================================
            // VALIDATION
            // ========================================

            if (
                    companyId.isEmpty()
                    || companyName.isEmpty()
                    || industry.isEmpty()
            ) {

                JOptionPane.showMessageDialog(

                        this,

                        "Company ID, Company Name and Industry are required."
                );

                return;
            }


            // ========================================
            // HIRING ROLES ARRAY
            // ========================================

            List<String> hiringRoles =
                    new ArrayList<>();


            if (!rolesText.isEmpty()) {

                String[] roles =
                        rolesText.split(",");


                for (
                        String role :
                        roles
                ) {

                    hiringRoles.add(
                            role.trim()
                    );
                }
            }


            // ========================================
            // DEPARTMENTS ARRAY
            // ========================================

            List<String> allowedDepartments =
                    new ArrayList<>();


            if (!departmentsText.isEmpty()) {

                String[] departments =
                        departmentsText
                        .split(",");


                for (
                        String department :
                        departments
                ) {

                    allowedDepartments.add(
                            department.trim()
                    );
                }
            }


            // ========================================
            // EMBEDDED LOCATION
            // ========================================

            Document location =
                    new Document(
                            "city",
                            city
                    )
                    .append(
                            "state",
                            state
                    );


            // ========================================
            // EMBEDDED ELIGIBILITY
            // ========================================

            Document eligibility =
                    new Document(
                            "minimumCGPA",
                            minimumCGPA
                    )
                    .append(
                            "allowedDepartments",
                            allowedDepartments
                    );


            // ========================================
            // COMPANY DOCUMENT
            // ========================================

            Document company =
                    new Document(
                            "companyId",
                            companyId
                    )
                    .append(
                            "companyName",
                            companyName
                    )
                    .append(
                            "industry",
                            industry
                    )
                    .append(
                            "location",
                            location
                    )
                    .append(
                            "hiringRoles",
                            hiringRoles
                    )
                    .append(
                            "eligibility",
                            eligibility
                    );


            // ========================================
            // INSERT INTO MONGODB
            // ========================================

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> companies =
                    database
                    .getCollection("companies");


            companies.insertOne(
                    company
            );


            JOptionPane.showMessageDialog(

                    this,

                    "Company added successfully!"
            );


            parent.refreshCompanies();


            dispose();

        }
        catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(

                    this,

                    "Minimum CGPA must be a valid number."
            );
        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Error adding company:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}

