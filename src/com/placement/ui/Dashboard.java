
package com.placement.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Dashboard() {

        // =================================================
        // WINDOW SETTINGS
        // =================================================

        setTitle("Placement Management System");

        setSize(900, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        // =================================================
        // HEADER
        // =================================================

        JLabel title =
                new JLabel(
                        "PLACEMENT MANAGEMENT SYSTEM",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        10,
                        25,
                        10
                )
        );

        add(
                title,
                BorderLayout.NORTH
        );


        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                25,
                                25
                        )
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        80,
                        40,
                        80
                )
        );


        // =================================================
        // CREATE BUTTONS
        // =================================================

        JButton studentButton =
                new JButton("STUDENTS");

        JButton companyButton =
                new JButton("COMPANIES");

        JButton driveButton =
                new JButton("JOB DRIVES");

        JButton applicationButton =
                new JButton("APPLICATIONS");

        JButton analyticsButton =
                new JButton("PLACEMENT ANALYTICS");

        JButton exitButton =
                new JButton("EXIT");


        // =================================================
        // BUTTON FONT
        // =================================================

        Font buttonFont =
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                );

        studentButton.setFont(buttonFont);

        companyButton.setFont(buttonFont);

        driveButton.setFont(buttonFont);

        applicationButton.setFont(buttonFont);

        analyticsButton.setFont(buttonFont);

        exitButton.setFont(buttonFont);


        // =================================================
        // ADD BUTTONS TO PANEL
        // =================================================

        buttonPanel.add(
                studentButton
        );

        buttonPanel.add(
                companyButton
        );

        buttonPanel.add(
                driveButton
        );

        buttonPanel.add(
                applicationButton
        );

        buttonPanel.add(
                analyticsButton
        );

        buttonPanel.add(
                exitButton
        );


        add(
                buttonPanel,
                BorderLayout.CENTER
        );


        // =================================================
        // FOOTER
        // =================================================

        JLabel footer =
                new JLabel(
                        "Java + MongoDB Placement System",
                        SwingConstants.CENTER
                );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        20,
                        10
                )
        );

        add(
                footer,
                BorderLayout.SOUTH
        );


        // =================================================
        // STUDENTS BUTTON
        // =================================================

        studentButton.addActionListener(
                e -> {

                    StudentPanel studentPanel =
                            new StudentPanel();

                    studentPanel.setVisible(true);
                }
        );


        // =================================================
        // COMPANIES BUTTON
        // =================================================

        companyButton.addActionListener(
                e -> {

                    CompanyPanel companyPanel =
                            new CompanyPanel();

                    companyPanel.setVisible(true);
                }
        );


        // =================================================
        // JOB DRIVES BUTTON
        // =================================================

        driveButton.addActionListener(
                e -> {

                    System.out.println(
                            "Job Drives Module Selected"
                    );

                    JobDrivePanel jobDrivePanel =
                            new JobDrivePanel();

                    jobDrivePanel.setVisible(
                            true
                    );
                }
        );


        // =================================================
        // APPLICATIONS BUTTON
        // =================================================

        applicationButton.addActionListener(
                e -> {

                    ApplicationPanel applicationPanel =
                            new ApplicationPanel();

                    applicationPanel.setVisible(
                            true
                    );
                }
        );


        // =================================================
        // PLACEMENT ANALYTICS BUTTON
        // =================================================

        analyticsButton.addActionListener(
                e -> {

                    PlacementAnalytics analytics =
                            new PlacementAnalytics();

                    analytics.setVisible(
                            true
                    );
                }
        );


        // =================================================
        // EXIT BUTTON
        // =================================================

        exitButton.addActionListener(
                e -> {

                    System.exit(0);

                }
        );
    }


    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    Dashboard dashboard =
                            new Dashboard();

                    dashboard.setVisible(
                            true
                    );
                }
        );
    }
}

