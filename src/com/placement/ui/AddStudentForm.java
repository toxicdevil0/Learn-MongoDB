
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

public class AddStudentForm extends JFrame {

    private StudentPanel parent;

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField graduationField;
    private JTextField cgpaField;
    private JTextField skillsField;
    private JTextField cityField;
    private JTextField stateField;

    private JComboBox<String> departmentBox;


    public AddStudentForm(
            StudentPanel parent
    ) {

        this.parent = parent;

        setTitle(
                "Add Student"
        );

        setSize(
                500,
                550
        );

        setLocationRelativeTo(
                parent
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createForm();
    }


    private void createForm() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                11,
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


        idField =
                new JTextField();

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
                idField
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


        JButton saveButton =
                new JButton(
                        "SAVE STUDENT"
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


        saveButton.addActionListener(
                e -> saveStudent()
        );


        cancelButton.addActionListener(
                e -> dispose()
        );
    }


    // ============================================
    // SAVE STUDENT
    // ============================================

    private void saveStudent() {

        try {

            String studentId =
                    idField
                    .getText()
                    .trim();


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


            // ========================================
            // VALIDATION
            // ========================================

            if (
                    studentId.isEmpty()
                    || name.isEmpty()
                    || email.isEmpty()
            ) {

                JOptionPane.showMessageDialog(

                        this,

                        "Student ID, Name and Email are required."
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
            // EMBEDDED ADDRESS
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
            // STUDENT DOCUMENT
            // ========================================

            Document student =
                    new Document(
                            "studentId",
                            studentId
                    )
                    .append(
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
            // INSERT
            // ========================================

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> students =
                    database
                    .getCollection("students");


            students.insertOne(
                    student
            );


            JOptionPane.showMessageDialog(

                    this,

                    "Student added successfully!"
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

                    "Error adding student:\n"
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}

