
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

public class CompanyPanel extends JFrame {

    private JTable companyTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;


    // ============================================
    // CONSTRUCTOR
    // ============================================

    public CompanyPanel() {

        setTitle("Company Management");

        setSize(1000, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadCompanies();
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
                        "COMPANY MANAGEMENT",
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

                "Company ID",
                "Company Name",
                "Industry",
                "City",
                "State",
                "Hiring Roles"

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


        companyTable =
                new JTable(tableModel);


        companyTable.setRowHeight(28);


        companyTable
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
                        companyTable
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
                        "Company ID:"
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
                        "+ Add Company"
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
                e -> searchCompany()
        );


        refreshButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadCompanies();
                }
        );


        addButton.addActionListener(
                e -> openAddCompanyForm()
        );


        updateButton.addActionListener(
                e -> updateCompany()
        );


        deleteButton.addActionListener(
                e -> deleteCompany()
        );


        backButton.addActionListener(
                e -> dispose()
        );
    }


    // ============================================
    // LOAD ALL COMPANIES
    // ============================================

    private void loadCompanies() {

        tableModel.setRowCount(0);

        try {

            MongoDatabase database =
                    MongoDBConnection
                    .getDatabase();


            MongoCollection<Document> companies =
                    database
                    .getCollection("companies");


            for (
                    Document company :
                    companies.find()
            ) {

                addCompanyToTable(
                        company
                );
            }

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Unable to load companies:\n"
                    + e.getMessage(),

                    "Database Error",

                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }


    // ============================================
    // ADD COMPANY TO TABLE
    // ============================================

    private void addCompanyToTable(
            Document company
    ) {

        // Embedded location object

        Document location =
                (Document)
                company.get("location");


        String city = "";
        String state = "";


        if (location != null) {

            city =
                    location.getString(
                            "city"
                    );

            state =
                    location.getString(
                            "state"
                    );
        }


        // Hiring roles array

        List<String> roles =
                (List<String>)
                company.get("hiringRoles");


        String hiringRoles = "";


        if (roles != null) {

            hiringRoles =
                    String.join(
                            ", ",
                            roles
                    );
        }


        tableModel.addRow(

                new Object[]{

                        company.getString(
                                "companyId"
                        ),

                        company.getString(
                                "companyName"
                        ),

                        company.getString(
                                "industry"
                        ),

                        city,

                        state,

                        hiringRoles
                }
        );
    }


    // ============================================
    // SEARCH COMPANY
    // ============================================

    private void searchCompany() {

        String companyId =
                searchField
                .getText()
                .trim();


        if (companyId.isEmpty()) {

            JOptionPane.showMessageDialog(

                    this,

                    "Enter Company ID."
            );

            return;
        }


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


            tableModel.setRowCount(0);


            if (company == null) {

                JOptionPane.showMessageDialog(

                        this,

                        "Company not found."
                );

                return;
            }


            addCompanyToTable(
                    company
            );

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
    // ADD COMPANY
    // ============================================

    private void openAddCompanyForm() {

        AddCompanyForm form =
                new AddCompanyForm(this);

        form.setVisible(true);
    }


    // ============================================
    // UPDATE COMPANY
    // ============================================

    private void updateCompany() {

        int selectedRow =
                companyTable.getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a company first."
            );

            return;
        }


        String companyId =
                tableModel
                .getValueAt(
                        selectedRow,
                        0
                )
                .toString();


        UpdateCompanyForm form =
                new UpdateCompanyForm(
                        this,
                        companyId
                );


        form.setVisible(true);
    }
    // ============================================
    // DELETE COMPANY
    // ============================================

    private void deleteCompany() {

        int selectedRow =
                companyTable
                .getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(

                    this,

                    "Please select a company first."
            );

            return;
        }


        String companyId =
                tableModel
                .getValueAt(
                        selectedRow,
                        0
                )
                .toString();


        int confirmation =
                JOptionPane.showConfirmDialog(

                        this,

                        "Delete company "
                        + companyId
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


            MongoCollection<Document> companies =
                    database
                    .getCollection("companies");


            companies.deleteOne(

                    new Document(
                            "companyId",
                            companyId
                    )
            );


            JOptionPane.showMessageDialog(

                    this,

                    "Company deleted successfully."
            );


            loadCompanies();

        }
        catch (Exception e) {

            JOptionPane.showMessageDialog(

                    this,

                    "Delete failed:\n"
                    + e.getMessage()
            );
        }
    }
    public void refreshCompanies() {

        searchField.setText("");

        loadCompanies();
    }
}

