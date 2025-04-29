# Notes Taking Application

A simple desktop application for creating, editing, and managing notes.

## Overview

This Notes Taking Application is a Java-based desktop application that allows users to create, edit, and delete notes. The application uses a MySQL database for data persistence and Swing for the user interface.

## Features

* Create new notes with title and content
* View a list of all saved notes
* Edit existing notes
* Delete unwanted notes
* Automatic timestamps for creation and modification

## Technical Stack

* **Language**: Java
* **UI Framework**: Java Swing
* **Database**: MySQL
* **JDBC Driver**: MySQL Connector/J

## Requirements

* Java 9 or higher
* MySQL Server
* MySQL Connector/J (JDBC driver)

## Setup Instructions

### Database Setup

1. Install MySQL if you haven't already
2. Run the SQL script to create the database and tables:

```bash
mysql -u root -p < NotesApp.sql
```

3. Update the database credentials in `src/db/DBConnection.java` if necessary

### Application Setup

1. Clone the repository:

```bash
git clone https://github.com/sher2005/URK23CS7002NotesTakingApp.git
cd URK23CS7002NotesTakingApp
```

2. Compile the application:

```bash
javac -d bin src/module-info.java src/db/*.java src/models/*.java src/ui/*.java src/main/*.java
```

3. Run the application:

```bash
java -cp bin:mysql-connector-java.jar main.Main
```

## Project Structure

* `src/db/` - Database connection management
* `src/models/` - Data models and database operations
* `src/ui/` - User interface components
* `src/main/` - Application entry point
* `NotesApp.sql` - Database schema and sample data

## Usage

1. Launch the application
2. Click "New Note" to create a new note
3. Enter a title and content for your note
4. Click "Save Note" to save your changes
5. Select a note from the list to view or edit it
6. Click "Delete" to remove a selected note

## Screenshots

### Main Application Window
![Main Application Window](![Image](https://github.com/user-attachments/assets/f7059de3-4078-4792-99b1-1501197a9697))

### Creating a New Note
![Creating a New Note](assets/images/new-note.png)

### Editing an Existing Note
![Editing a Note](assets/images/edit-note.png)

## Future Enhancements

* User authentication and multi-user support
* Note categories and tags
* Rich text formatting
* Search functionality
* Export/import notes

## License

*[License information would be added here]*

## Contributors

*[Contributor information would be added here]*
