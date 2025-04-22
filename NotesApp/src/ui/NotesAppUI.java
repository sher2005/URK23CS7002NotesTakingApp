package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import db.DBConnection;
import models.Note;

public class NotesAppUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JList<Note> notesList;
    private DefaultListModel<Note> listModel;
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton saveButton;
    private JButton newButton;
    private JButton deleteButton;
    
    private Note currentNote;
    private boolean isNewNote = false;
    
    public NotesAppUI() {
        setTitle("Notes Taking App");
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Close database connection when application closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBConnection.closeConnection();
            }
        });
        
        setupUI();
        loadNotes();
    }
    
    private void setupUI() {
        // Main layout
        setLayout(new BorderLayout(10, 10));
        
        // Notes list panel (left side)
        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        
        JLabel listLabel = new JLabel("My Notes");
        listPanel.add(listLabel, BorderLayout.NORTH);
        
        listModel = new DefaultListModel<>();
        notesList = new JList<>(listModel);
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(notesList);
        listPanel.add(listScrollPane, BorderLayout.CENTER);
        
        // Buttons for list panel
        JPanel listButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newButton = new JButton("New Note");
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        
        listButtonsPanel.add(newButton);
        listButtonsPanel.add(deleteButton);
        listPanel.add(listButtonsPanel, BorderLayout.SOUTH);
        
        // Note details panel (right side)
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Title:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        titleField = new JTextField(20);
        detailsPanel.add(titleField, gbc);
        
        // Content area
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        detailsPanel.add(new JLabel("Content:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setPreferredSize(new Dimension(300, 400));
        detailsPanel.add(contentScrollPane, gbc);
        
        // Save button
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        saveButton = new JButton("Save Note");
        detailsPanel.add(saveButton, gbc);
        
        // Add panels to main frame
        add(listPanel, BorderLayout.WEST);
        add(detailsPanel, BorderLayout.CENTER);
        
        // Initially disable editing
        setFieldsEnabled(false);
        
        // Set up event listeners
        setupEventListeners();
    }
    
    private void setupEventListeners() {
        // List selection listener
        notesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Note selectedNote = notesList.getSelectedValue();
                    if (selectedNote != null) {
                        currentNote = selectedNote;
                        displayNote(currentNote);
                        isNewNote = false;
                        setFieldsEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                }
            }
        });
        
        // New button listener
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentNote = new Note("", "");
                isNewNote = true;
                notesList.clearSelection();
                
                // Clear and enable fields
                titleField.setText("");
                contentArea.setText("");
                setFieldsEnabled(true);
                deleteButton.setEnabled(false);
                
                // Set focus to title field
                titleField.requestFocusInWindow();
            }
        });
        
        // Save button listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentNote();
            }
        });
        
        // Delete button listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCurrentNote();
            }
        });
    }
    
    private void displayNote(Note note) {
        titleField.setText(note.getTitle());
        contentArea.setText(note.getContent());
    }
    
    private void setFieldsEnabled(boolean enabled) {
        titleField.setEnabled(enabled);
        contentArea.setEnabled(enabled);
        saveButton.setEnabled(enabled);
    }
    
    private void saveCurrentNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        
        // Validate title
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a title for your note", 
                    "Title Required", 
                    JOptionPane.WARNING_MESSAGE);
            titleField.requestFocusInWindow();
            return;
        }
        
        // Update the note object
        currentNote.setTitle(title);
        currentNote.setContent(content);
        
        // Save to database
        if (currentNote.save()) {
            if (isNewNote) {
                // Add to the list model
                listModel.addElement(currentNote);
                isNewNote = false;
            } else {
                // Refresh the list to show updated title
                int selectedIndex = notesList.getSelectedIndex();
                listModel.set(selectedIndex, currentNote);
            }
            JOptionPane.showMessageDialog(this, 
                    "Note saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Failed to save note. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCurrentNote() {
        if (currentNote != null && currentNote.getId() > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this note?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (currentNote.delete()) {
                    int selectedIndex = notesList.getSelectedIndex();
                    listModel.remove(selectedIndex);
                    
                    // Clear fields and disable them
                    titleField.setText("");
                    contentArea.setText("");
                    setFieldsEnabled(false);
                    deleteButton.setEnabled(false);
                    currentNote = null;
                    
                    JOptionPane.showMessageDialog(this,
                            "Note deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete note. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void loadNotes() {
        // Clear existing list
        listModel.clear();
        
        // Load notes from database
        List<Note> notes = Note.getAllNotes();
        
        if (!notes.isEmpty()) {
            // Add notes to list model
            for (Note note : notes) {
                listModel.addElement(note);
            }
        }
    }
}