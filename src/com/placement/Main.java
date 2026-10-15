package com.placement;

import javax.swing.SwingUtilities;

import com.placement.ui.Dashboard;

public class Main {

    public static void main(String[] args) {

        MongoDBIndexes.createIndexes();

        SwingUtilities.invokeLater(() -> {

            Dashboard dashboard =
                    new Dashboard();

            dashboard.setVisible(true);
        });
    }
}