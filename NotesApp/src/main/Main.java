package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import db.DBConnection;
import ui.NotesAppUI;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Initialize database connection
        DBConnection.getConnection();
        
        // Start UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NotesAppUI().setVisible(true);
            }
        });
    }
}