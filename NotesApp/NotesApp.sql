-- Create database
CREATE DATABASE IF NOT EXISTS notesdb;

-- Use the database
USE notesdb;

-- Create notes table
CREATE TABLE IF NOT EXISTS notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert some sample notes
INSERT INTO notes (title, content) VALUES 
('Welcome to Notes App', 'This is your first note! You can create, edit and delete notes using this application.'),
('Shopping List', 'Milk\nEggs\nBread\nFruits\nVegetables'),
('Project Ideas', 'Here are some project ideas for the future:\n- Weather app\n- To-do list\n- Recipe manager\n- Expense tracker');