package com.placement.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.placement.MongoDBConnection;

public class ApplicationPanel extends JFrame {

    private JTable applicationTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ApplicationPanel() {

        setTitle("Application Management");

        setSize(1200, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadApplications();
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
                        "APPLICATION MANAGEMENT",
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

                "Application ID",
                "Student ID",
                "Drive ID",
                "Application Date",
                "Status",
                "Rounds"
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

        applicationTable =
                new JTable(tableModel);

        applicationTable.setRowHeight(28);

        applicationTable
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
                        applicationTable
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
                        "Application ID:"
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
                        "+ Add Application"
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
                e -> searchApplication()
        );

        refreshButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadApplications();
                }
        );

        addButton.addActionListener(
                e -> openAddApplicationForm()
        );

        updateButton.addActionListener(
                e -> updateApplication()
        );

        deleteButton.addActionListener(
                e -> deleteApplication()
        );

        backButton.addActionListener(
                e -> dispose()
        );
    }

    // =====================================================
    // LOAD APPLICATIONS
    // =====================================================

    private void loadApplications() {

        tableModel.setRowCount(0);

        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();

            MongoCollection<Document> applications =
                    database.getCollection(
                            "applications"
                    );

            for (Document application :
                    applications.find()) {

                addApplicationToTable(
                        application
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load applications:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // ADD APPLICATION TO TABLE
    // =====================================================

    private void addApplicationToTable(
            Document application
    ) {

        String applicationId =
                application.getString(
                        "applicationId"
                );

        String studentId =
                application.getString(
                        "studentId"
                );

        String driveId =
                application.getString(
                        "driveId"
                );

        String applicationDate =
                "";

        Object dateObject =
                application.get(
                        "applicationDate"
                );

        if (dateObject != null) {

            applicationDate =
                    dateObject.toString();
        }

        String status =
                application.getString(
                        "status"
                );

        // =================================================
        // ROUNDS
        // =================================================

        String roundsValue = "";

        Object roundsObject =
                application.get(
                        "rounds"
                );

        if (roundsObject instanceof List<?>) {

            List<?> rounds =
                    (List<?>) roundsObject;

            StringBuilder roundsBuilder =
                    new StringBuilder();

            for (Object roundObject :
                    rounds) {

                if (roundObject instanceof Document) {

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

                    if (roundsBuilder.length() > 0) {

                        roundsBuilder.append(
                                " | "
                        );
                    }

                    roundsBuilder
                            .append("R")
                            .append(roundNumber)
                            .append(": ")
                            .append(type)
                            .append(" - ")
                            .append(result);
                }
            }

            roundsValue =
                    roundsBuilder.toString();
        }

        tableModel.addRow(
                new Object[]{

                        applicationId,

                        studentId,

                        driveId,

                        applicationDate,

                        status,

                        roundsValue
                }
        );
    }

    // =====================================================
    // SEARCH APPLICATION
    // =====================================================

    private void searchApplication() {

        String applicationId =
                searchField
                        .getText()
                        .trim();

        if (applicationId.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter Application ID."
            );

            return;
        }

        try {

            MongoDatabase database =
                    MongoDBConnection.getDatabase();

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

            tableModel.setRowCount(0);

            if (application == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Application not found."
                );

                return;
            }

            addApplicationToTable(
                    application
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // ADD APPLICATION
    // =====================================================

    private void openAddApplicationForm() {

        AddApplicationForm form =
                new AddApplicationForm(this);

        form.setVisible(true);
    }

    // =====================================================
    // UPDATE APPLICATION
    // =====================================================

    private void updateApplication() {

        int selectedRow =
                applicationTable
                        .getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first."
            );

            return;
        }

        String applicationId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();

        UpdateApplicationForm form =
                new UpdateApplicationForm(
                        this,
                        applicationId
                );

        form.setVisible(true);
    }

    // =====================================================
    // DELETE APPLICATION
    // =====================================================

    private void deleteApplication() {

        int selectedRow =
                applicationTable
                        .getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first."
            );

            return;
        }

        String applicationId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();

        int confirmation =
                JOptionPane.showConfirmDialog(

                        this,

                        "Delete application "
                                + applicationId
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
                    MongoDBConnection.getDatabase();

            MongoCollection<Document> applications =
                    database.getCollection(
                            "applications"
                    );

            applications.deleteOne(
                    new Document(
                            "applicationId",
                            applicationId
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Application deleted successfully."
            );

            loadApplications();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Delete failed:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // REFRESH
    // =====================================================

    public void refreshApplications() {

        searchField.setText("");

        loadApplications();
    }
}