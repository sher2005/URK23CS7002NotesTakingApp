package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;

public class Note {
    private int id;
    private String title;
    private String content;
    
    // Constructor for new notes (no ID yet)
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    // Constructor for existing notes (with ID)
    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    // Save note to database
    public boolean save() {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        
        try {
            if (id == 0) {
                // Insert new note
                String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, title);
                ps.setString(2, content);
                
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 1) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        this.id = rs.getInt(1);
                    }
                    rs.close();
                    return true;
                }
            } else {
                // Update existing note
                String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, title);
                ps.setString(2, content);
                ps.setInt(3, id);
                
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saving note: " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
        return false;
    }
    
    // Delete note from database
    public boolean delete() {
        if (id == 0) {
            return false;
        }
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        
        try {
            String sql = "DELETE FROM notes WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting note: " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
        return false;
    }
    
    // Retrieve all notes from database
    public static List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT id, title, content FROM notes ORDER BY id DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Note note = new Note(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("content")
                );
                notes.add(note);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving notes: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return notes;
    }
    
    // Find note by ID
    public static Note findById(int id) {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT id, title, content FROM notes WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Note(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("content")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding note: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return title;
    }
}