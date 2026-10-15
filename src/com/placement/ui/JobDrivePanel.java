
package com.placement.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

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

public class JobDrivePanel extends JFrame {

    private JTable driveTable;

    private DefaultTableModel tableModel;

    private JTextField searchField;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public JobDrivePanel() {

        setTitle("Job Drive Management");

        setSize(1100, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        // Load data AFTER UI is completely created
        loadJobDrives();
    }


    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        setLayout(
                new BorderLayout(10, 10)
        );


        // =================================================
        // TITLE
        // =================================================

        JLabel title =
                new JLabel(
                        "JOB DRIVE MANAGEMENT",
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


        // =================================================
        // TABLE
        // =================================================

        String[] columns = {

                "Drive ID",
                "Company ID",
                "Role",
                "Package",
                "Eligibility CGPA",
                "Skills Required",
                "Status"
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


        driveTable =
                new JTable(
                        tableModel
                );


        driveTable.setRowHeight(28);


        driveTable
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
                        driveTable
                );


        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =================================================
        // BOTTOM PANEL
        // =================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );


        // =================================================
        // SEARCH PANEL
        // =================================================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );


        JLabel searchLabel =
                new JLabel(
                        "Drive ID:"
                );


        searchField =
                new JTextField(
                        15
                );


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
                        "+ Add Job Drive"
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


        // =================================================
        // ACTION PANEL
        // =================================================

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


        // =================================================
        // BUTTON EVENTS
        // =================================================

        searchButton.addActionListener(
                e -> searchJobDrive()
        );


        refreshButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadJobDrives();
                }
        );


        addButton.addActionListener(
                e -> openAddJobDriveForm()
        );


        updateButton.addActionListener(
                e -> updateJobDrive()
        );


        deleteButton.addActionListener(
                e -> deleteJobDrive()
        );


        backButton.addActionListener(
                e -> dispose()
        );
    }


    // =====================================================
    // LOAD JOB DRIVES
    // =====================================================

    private void loadJobDrives() {

        tableModel.setRowCount(0);

        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();


            if (database == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "MongoDB database connection is null.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            MongoCollection<Document> drives =
                    database.getCollection(
                            "job_drives"
                    );


            for (Document drive :
                    drives.find()) {

                addDriveToTable(
                        drive
                );
            }


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load job drives:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // ADD DRIVE TO TABLE
    // =====================================================

    private void addDriveToTable(
            Document drive
    ) {

        if (drive == null) {
            return;
        }


        // =================================================
        // DRIVE ID
        // =================================================

        String driveId =
                drive.getString(
                        "driveId"
                );


        // =================================================
        // COMPANY ID
        // =================================================

        String companyId =
                drive.getString(
                        "companyId"
                );


        // =================================================
        // ROLE
        // =================================================

        String role =
                drive.getString(
                        "role"
                );


        // =================================================
        // PACKAGE
        // =================================================

        String packageValue = "";

        Object packageObject =
                drive.get(
                        "package"
                );


        if (packageObject instanceof Document) {

            Document packageDocument =
                    (Document) packageObject;


            Number ctc =
                    packageDocument.get(
                            "ctc",
                            Number.class
                    );


            String currency =
                    packageDocument.getString(
                            "currency"
                    );


            if (ctc != null) {

                packageValue =
                        String.format(
                                "%.2f %s",
                                ctc.doubleValue(),
                                currency != null
                                        ? currency
                                        : ""
                        );
            }

        } else if (packageObject != null) {

            packageValue =
                    packageObject.toString();
        }


        // =================================================
        // SKILLS
        // =================================================

        String skillsValue = "";

        Object skillsObject =
                drive.get(
                        "skillsRequired"
                );


        if (skillsObject instanceof List<?>) {

            List<?> skills =
                    (List<?>) skillsObject;


            StringBuilder builder =
                    new StringBuilder();


            for (int i = 0;
                 i < skills.size();
                 i++) {

                if (i > 0) {

                    builder.append(", ");
                }

                builder.append(
                        String.valueOf(
                                skills.get(i)
                        )
                );
            }


            skillsValue =
                    builder.toString();
        }


        // =================================================
        // ELIGIBILITY CGPA
        // =================================================

        String cgpaValue = "";

        Number eligibilityCGPA =
                drive.get(
                        "eligibilityCGPA",
                        Number.class
                );


        if (eligibilityCGPA != null) {

            cgpaValue =
                    String.format(
                            "%.2f",
                            eligibilityCGPA.doubleValue()
                    );
        }


        // =================================================
        // STATUS
        // =================================================

        String status =
                drive.getString(
                        "status"
                );


        // =================================================
        // ADD TO TABLE
        // =================================================

        tableModel.addRow(
                new Object[]{

                        driveId != null
                                ? driveId
                                : "",

                        companyId != null
                                ? companyId
                                : "",

                        role != null
                                ? role
                                : "",

                        packageValue,

                        cgpaValue,

                        skillsValue,

                        status != null
                                ? status
                                : ""
                }
        );
    }


    // =====================================================
    // SEARCH JOB DRIVE
    // =====================================================

    private void searchJobDrive() {

        String driveId =
                searchField
                        .getText()
                        .trim();


        if (driveId.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter Drive ID."
            );

            return;
        }


        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();


            MongoCollection<Document> drives =
                    database.getCollection(
                            "job_drives"
                    );


            Document drive =
                    drives.find(
                            new Document(
                                    "driveId",
                                    driveId
                            )
                    ).first();


            tableModel.setRowCount(0);


            if (drive == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Job Drive not found."
                );

                return;
            }


            addDriveToTable(
                    drive
            );


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed:\n"
                            + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // ADD JOB DRIVE
    // =====================================================

    private void openAddJobDriveForm() {

        try {

            AddJobDriveForm form =
                    new AddJobDriveForm(
                            this
                    );

            form.setVisible(true);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Add Job Drive form:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // UPDATE JOB DRIVE
    // =====================================================

    private void updateJobDrive() {

        int selectedRow =
                driveTable.getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a job drive first."
            );

            return;
        }


        String driveId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();


        try {

            UpdateJobDriveForm form =
                    new UpdateJobDriveForm(
                            this,
                            driveId
                    );

            form.setVisible(true);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open Update Job Drive form:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // DELETE JOB DRIVE
    // =====================================================

    private void deleteJobDrive() {

        int selectedRow =
                driveTable.getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a job drive first."
            );

            return;
        }


        String driveId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();


        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete job drive "
                                + driveId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );


        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }


        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();


            MongoCollection<Document> drives =
                    database.getCollection(
                            "job_drives"
                    );


            drives.deleteOne(
                    new Document(
                            "driveId",
                            driveId
                    )
            );


            JOptionPane.showMessageDialog(
                    this,
                    "Job drive deleted successfully."
            );


            loadJobDrives();


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Delete failed:\n"
                            + e.getMessage(),
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // REFRESH
    // =====================================================

    public void refreshJobDrives() {

        if (searchField != null) {

            searchField.setText("");
        }

        loadJobDrives();
    }
}

