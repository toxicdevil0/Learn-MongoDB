
package com.placement.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class StudentPanel extends JFrame {

    private JTable studentTable;

    private DefaultTableModel tableModel;

    private JTextField searchField;


    // ============================================
    // CONSTRUCTOR
    // ============================================

    public StudentPanel() {

        setTitle("Student Management");

        setSize(1000, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadStudents();
    }


    // ============================================
    // CREATE UI
    // ============================================

    private void createUI() {

        setLayout(
                new BorderLayout(10, 10)
        );


        // ========================================
        // TITLE
        // ========================================

        JLabel title =
                new JLabel(
                        "STUDENT MANAGEMENT",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        15,
                        10
                )
        );

        add(
                title,
                BorderLayout.NORTH
        );


        // ========================================
        // TABLE
        // ========================================

        String[] columns = {

                "Student ID",
                "Name",
                "Email",
                "Department",
                "Graduation Year",
                "CGPA"

        };


        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };


        studentTable =
                new JTable(tableModel);


        studentTable.setRowHeight(28);


        studentTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );


        JScrollPane scrollPane =
                new JScrollPane(
                        studentTable
                );


        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // ========================================
        // BOTTOM PANEL
        // ========================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );


        // ========================================
        // SEARCH PANEL
        // ========================================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );


        JLabel searchLabel =
                new JLabel(
                        "Student ID:"
                );


        searchField =
                new JTextField(15);


        JButton searchButton =
                new JButton(
                        "Search"
                );


        JButton refreshButton =
                new JButton(
                        "Refresh"
                );


        JButton addButton =
                new JButton(
                        "+ Add Student"
                );


        searchPanel.add(
                searchLabel
        );

        searchPanel.add(
                searchField
        );

        searchPanel.add(
                searchButton
        );

        searchPanel.add(
                refreshButton
        );

        searchPanel.add(
                addButton
        );


        // ========================================
        // ACTION PANEL
        // ========================================

        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );


        JButton updateButton =
                new JButton(
                        "Update"
                );


        JButton deleteButton =
                new JButton(
                        "Delete"
                );


        JButton backButton =
                new JButton(
                        "Back"
                );


        actionPanel.add(
                updateButton
        );

        actionPanel.add(
                deleteButton
        );

        actionPanel.add(
                backButton
        );


        bottomPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );


        bottomPanel.add(
                actionPanel,
                BorderLayout.SOUTH
        );


        add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // ========================================
        // BUTTON EVENTS
        // ========================================

        searchButton.addActionListener(
                e -> searchStudent()
        );


        refreshButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadStudents();
                }
        );


        addButton.addActionListener(
                e -> openAddStudentForm()
        );


        updateButton.addActionListener(
                e -> updateStudent()
        );


        deleteButton.addActionListener(
                e -> deleteStudent()
        );


        backButton.addActionListener(
                e -> dispose()
        );
    }


    // ============================================
    // LOAD ALL STUDENTS
    // ============================================

    private void loadStudents() {

        tableModel.setRowCount(0);

        try {

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> students =
                    database
                    .getCollection("students");


            for (
                    Document student :
                    students.find()
            ) {

                addStudentToTable(
                        student
                );
            }

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Unable to load students:\n"
                    + e.getMessage(),

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // ============================================
    // ADD STUDENT TO TABLE
    // ============================================

    private void addStudentToTable(Document student) {

        Number cgpaNumber =
                student.get("cgpa", Number.class);

        Double cgpa = null;

        if (cgpaNumber != null) {
            cgpa = cgpaNumber.doubleValue();
        }

        tableModel.addRow(
                new Object[]{

                        student.getString("studentId"),

                        student.getString("name"),

                        student.getString("email"),

                        student.getString("department"),

                        student.getInteger("graduationYear"),

                        cgpa
                }
        );
    }

    // ============================================
    // SEARCH STUDENT
    // ============================================

    private void searchStudent() {

        String studentId =
                searchField.getText().trim();

        if (studentId.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter Student ID."
            );

            return;
        }

        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();

            MongoCollection<Document> students =
                    database.getCollection("students");

            Document student =
                    students.find(
                            new Document(
                                    "studentId",
                                    studentId
                            )
                    ).first();

            tableModel.setRowCount(0);

            if (student == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Student not found."
                );

                return;
            }

            addStudentToTable(student);

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed:\n"
                    + e.getMessage()
            );
        }
    }

    // ============================================
    // OPEN ADD STUDENT FORM
    // ============================================

    private void openAddStudentForm() {

        AddStudentForm form =
                new AddStudentForm(this);

        form.setVisible(true);
    }


    // ============================================
    // UPDATE STUDENT
    // ============================================

    private void updateStudent() {

        int selectedRow =
                studentTable
                .getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(

                    this,

                    "Please select a student first."
            );

            return;
        }


        String studentId =
                tableModel
                .getValueAt(
                        selectedRow,
                        0
                )
                .toString();


        UpdateStudentForm form =
                new UpdateStudentForm(
                        this,
                        studentId
                );


        form.setVisible(true);
    }


    // ============================================
    // DELETE STUDENT
    // ============================================

    private void deleteStudent() {

        int selectedRow =
                studentTable
                .getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(

                    this,

                    "Please select a student first."
            );

            return;
        }


        String studentId =
                tableModel
                .getValueAt(
                        selectedRow,
                        0
                )
                .toString();


        int confirmation =
                JOptionPane.showConfirmDialog(

                        this,

                        "Delete student "
                        + studentId
                        + "?",

                        "Confirm Delete",

                        JOptionPane.YES_NO_OPTION
                );


        if (
                confirmation
                != JOptionPane.YES_OPTION
        ) {

            return;
        }


        try {

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> students =
                    database
                    .getCollection("students");


            students.deleteOne(

                    new Document(
                            "studentId",
                            studentId
                    )
            );


            JOptionPane.showMessageDialog(

                    this,

                    "Student deleted successfully."
            );


            loadStudents();

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Delete failed:\n"
                    + e.getMessage()
            );
        }
    }


    // ============================================
    // REFRESH TABLE
    // ============================================

    public void refreshStudents() {

        searchField.setText("");

        loadStudents();
    }
}

