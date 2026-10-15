
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

public class UpdateStudentForm extends JFrame {

    private StudentPanel parent;

    private String studentId;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField graduationField;
    private JTextField cgpaField;
    private JTextField skillsField;
    private JTextField cityField;
    private JTextField stateField;

    private JComboBox<String> departmentBox;


    public UpdateStudentForm(
            StudentPanel parent,
            String studentId
    ) {

        this.parent = parent;

        this.studentId = studentId;

        setTitle(
                "Update Student - "
                + studentId
        );

        setSize(
                500,
                520
        );

        setLocationRelativeTo(
                parent
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createForm();

        loadStudent();
    }


    // ============================================
    // CREATE FORM
    // ============================================

    private void createForm() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                10,
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


        nameField =
                new JTextField();


        emailField =
                new JTextField();


        phoneField =
                new JTextField();


        graduationField =
                new JTextField();


        cgpaField =
                new JTextField();


        skillsField =
                new JTextField();


        cityField =
                new JTextField();


        stateField =
                new JTextField();


        departmentBox =
                new JComboBox<>(
                        new String[]{

                                "CSE",
                                "ECE",
                                "EEE",
                                "MECH",
                                "CIVIL",
                                "IT"

                        }
                );


        panel.add(
                new JLabel("Student ID:")
        );

        panel.add(
                new JLabel(studentId)
        );


        panel.add(
                new JLabel("Name:")
        );

        panel.add(
                nameField
        );


        panel.add(
                new JLabel("Email:")
        );

        panel.add(
                emailField
        );


        panel.add(
                new JLabel("Phone:")
        );

        panel.add(
                phoneField
        );


        panel.add(
                new JLabel("Department:")
        );

        panel.add(
                departmentBox
        );


        panel.add(
                new JLabel("Graduation Year:")
        );

        panel.add(
                graduationField
        );


        panel.add(
                new JLabel("CGPA:")
        );

        panel.add(
                cgpaField
        );


        panel.add(
                new JLabel("Skills:")
        );

        panel.add(
                skillsField
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


        JButton updateButton =
                new JButton(
                        "UPDATE STUDENT"
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


        updateButton.addActionListener(
                e -> updateStudent()
        );


        cancelButton.addActionListener(
                e -> dispose()
        );
    }


    // ============================================
    // LOAD EXISTING STUDENT
    // ============================================

    private void loadStudent() {

        try {

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> students =
                    database
                    .getCollection("students");


            Document student =
                    students.find(

                            new Document(
                                    "studentId",
                                    studentId
                            )

                    ).first();


            if (student == null) {

                JOptionPane.showMessageDialog(

                        this,

                        "Student not found."
                );

                dispose();

                return;
            }


            nameField.setText(
                    student.getString("name")
            );


            emailField.setText(
                    student.getString("email")
            );


            phoneField.setText(
                    student.getString("phone")
            );


            Integer year =
                    student.getInteger(
                            "graduationYear"
                    );


            if (year != null) {

                graduationField.setText(
                        String.valueOf(year)
                );
            }


            Number cgpaNumber =
                    student.get("cgpa", Number.class);

            if (cgpaNumber != null) {

                cgpaField.setText(
                        String.valueOf(
                                cgpaNumber.doubleValue()
                        )
                );
            }


            String department =
                    student.getString(
                            "department"
                    );


            if (department != null) {

                departmentBox.setSelectedItem(
                        department
                );
            }


            // ========================================
            // LOAD SKILLS ARRAY
            // ========================================

            List<String> skills =
                    (List<String>)
                    student.get("skills");


            if (skills != null) {

                skillsField.setText(
                        String.join(
                                ", ",
                                skills
                        )
                );
            }


            // ========================================
            // LOAD EMBEDDED ADDRESS
            // ========================================

            Document address =
                    (Document)
                    student.get("address");


            if (address != null) {

                String city =
                        address.getString(
                                "city"
                        );


                String state =
                        address.getString(
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

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Unable to load student:\n"
                    + e.getMessage()
            );
        }
    }


    // ============================================
    // UPDATE STUDENT
    // ============================================

    private void updateStudent() {

        try {

            String name =
                    nameField
                    .getText()
                    .trim();


            String email =
                    emailField
                    .getText()
                    .trim();


            String phone =
                    phoneField
                    .getText()
                    .trim();


            String department =
                    departmentBox
                    .getSelectedItem()
                    .toString();


            int graduationYear =
                    Integer.parseInt(

                            graduationField
                            .getText()
                            .trim()
                    );


            double cgpa =
                    Double.parseDouble(

                            cgpaField
                            .getText()
                            .trim()
                    );


            String skillsText =
                    skillsField
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


            if (
                    name.isEmpty()
                    || email.isEmpty()
            ) {

                JOptionPane.showMessageDialog(

                        this,

                        "Name and Email are required."
                );

                return;
            }


            // ========================================
            // SKILLS ARRAY
            // ========================================

            List<String> skills =
                    new ArrayList<>();


            if (!skillsText.isEmpty()) {

                String[] skillArray =
                        skillsText.split(",");


                for (
                        String skill :
                        skillArray
                ) {

                    skills.add(
                            skill.trim()
                    );
                }
            }


            // ========================================
            // ADDRESS OBJECT
            // ========================================

            Document address =
                    new Document(
                            "city",
                            city
                    )
                    .append(
                            "state",
                            state
                    );


            // ========================================
            // UPDATE DOCUMENT
            // ========================================

            Document updatedData =
                    new Document(
                            "name",
                            name
                    )
                    .append(
                            "email",
                            email
                    )
                    .append(
                            "phone",
                            phone
                    )
                    .append(
                            "department",
                            department
                    )
                    .append(
                            "graduationYear",
                            graduationYear
                    )
                    .append(
                            "cgpa",
                            cgpa
                    )
                    .append(
                            "skills",
                            skills
                    )
                    .append(
                            "address",
                            address
                    );


            // ========================================
            // UPDATE MONGODB
            // ========================================

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> students =
                    database
                    .getCollection("students");


            students.updateOne(

                    new Document(
                            "studentId",
                            studentId
                    ),

                    new Document(
                            "$set",
                            updatedData
                    )
            );


            JOptionPane.showMessageDialog(

                    this,

                    "Student updated successfully!"
            );


            parent.refreshStudents();


            dispose();

        }
        catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(

                    this,

                    "Graduation Year and CGPA must be valid numbers."
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

