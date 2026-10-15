
package com.placement.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class UpdateCompanyForm extends JFrame {

    private CompanyPanel parent;

    private String companyId;

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

    public UpdateCompanyForm(
            CompanyPanel parent,
            String companyId
    ) {

        this.parent = parent;

        this.companyId = companyId;

        setTitle(
                "Update Company - "
                + companyId
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

        loadCompany();
    }


    // ============================================
    // CREATE FORM
    // ============================================

    private void createForm() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                9,
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
        // COMPANY ID
        // ========================================

        panel.add(
                new JLabel("Company ID:")
        );

        panel.add(
                new JLabel(companyId)
        );


        // ========================================
        // COMPANY NAME
        // ========================================

        panel.add(
                new JLabel("Company Name:")
        );

        panel.add(
                companyNameField
        );


        // ========================================
        // INDUSTRY
        // ========================================

        panel.add(
                new JLabel("Industry:")
        );

        panel.add(
                industryField
        );


        // ========================================
        // CITY
        // ========================================

        panel.add(
                new JLabel("City:")
        );

        panel.add(
                cityField
        );


        // ========================================
        // STATE
        // ========================================

        panel.add(
                new JLabel("State:")
        );

        panel.add(
                stateField
        );


        // ========================================
        // HIRING ROLES
        // ========================================

        panel.add(
                new JLabel("Hiring Roles:")
        );

        panel.add(
                rolesField
        );


        // ========================================
        // MINIMUM CGPA
        // ========================================

        panel.add(
                new JLabel("Minimum CGPA:")
        );

        panel.add(
                minimumCGPAField
        );


        // ========================================
        // ALLOWED DEPARTMENTS
        // ========================================

        panel.add(
                new JLabel("Allowed Departments:")
        );

        panel.add(
                departmentsField
        );


        JButton updateButton =
                new JButton(
                        "UPDATE COMPANY"
                );


        JButton cancelButton =
                new JButton(
                        "CANCEL"
                );


        panel.add(
                updateButton
        );

        panel.add(
                cancelButton
        );


        add(panel);


        // ========================================
        // BUTTON EVENTS
        // ========================================

        updateButton.addActionListener(
                e -> updateCompany()
        );


        cancelButton.addActionListener(
                e -> dispose()
        );
    }


    // ============================================
    // LOAD EXISTING COMPANY
    // ============================================

    private void loadCompany() {

        try {

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> companies =
                    database
                    .getCollection("companies");


            Document company =
                    companies.find(

                            new Document(
                                    "companyId",
                                    companyId
                            )

                    ).first();


            if (company == null) {

                JOptionPane.showMessageDialog(

                        this,

                        "Company not found."
                );

                dispose();

                return;
            }


            // ========================================
            // BASIC DETAILS
            // ========================================

            companyNameField.setText(
                    company.getString(
                            "companyName"
                    )
            );


            industryField.setText(
                    company.getString(
                            "industry"
                    )
            );


            // ========================================
            // LOCATION
            // ========================================

            Document location =
                    (Document)
                    company.get(
                            "location"
                    );


            if (location != null) {

                String city =
                        location.getString(
                                "city"
                        );


                String state =
                        location.getString(
                                "state"
                        );


                cityField.setText(
                        city == null
                        ? ""
                        : city
                );


                stateField.setText(
                        state == null
                        ? ""
                        : state
                );
            }


            // ========================================
            // HIRING ROLES ARRAY
            // ========================================

            List<String> roles =
                    (List<String>)
                    company.get(
                            "hiringRoles"
                    );


            if (roles != null) {

                rolesField.setText(
                        String.join(
                                ", ",
                                roles
                        )
                );
            }


            // ========================================
            // ELIGIBILITY
            // ========================================

            Document eligibility =
                    (Document)
                    company.get(
                            "eligibility"
                    );


            if (eligibility != null) {

                Number minimumCGPA =
                        eligibility.get(
                                "minimumCGPA",
                                Number.class
                        );


                if (minimumCGPA != null) {

                    minimumCGPAField.setText(
                            String.valueOf(
                                    minimumCGPA.doubleValue()
                            )
                    );
                }


                List<String> departments =
                        (List<String>)
                        eligibility.get(
                                "allowedDepartments"
                        );


                if (departments != null) {

                    departmentsField.setText(
                            String.join(
                                    ", ",
                                    departments
                            )
                    );
                }
            }

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Unable to load company:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }


    // ============================================
    // UPDATE COMPANY
    // ============================================

    private void updateCompany() {

        try {

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
                    companyName.isEmpty()
                    || industry.isEmpty()
            ) {

                JOptionPane.showMessageDialog(

                        this,

                        "Company Name and Industry are required."
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
            // LOCATION OBJECT
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
            // ELIGIBILITY OBJECT
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
            // UPDATED DATA
            // ========================================

            Document updatedData =
                    new Document(
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
            // UPDATE MONGODB
            // ========================================

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> companies =
                    database
                    .getCollection("companies");


            companies.updateOne(

                    new Document(
                            "companyId",
                            companyId
                    ),

                    new Document(
                            "$set",
                            updatedData
                    )
            );


            JOptionPane.showMessageDialog(

                    this,

                    "Company updated successfully!"
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

                    "Update failed:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}

