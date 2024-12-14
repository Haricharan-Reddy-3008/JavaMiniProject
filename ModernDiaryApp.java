
////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class ModernDiaryApp extends JFrame {
    private JTextArea textArea;
    private JButton saveButton, viewButton, deleteButton, renameButton, editButton, changeFolderButton, openFolderButton;
    private DefaultListModel<String> entriesListModel;
    private JList<String> entriesList;
    private DiaryManager diaryManager;

    public ModernDiaryApp() {


        if (!showPasswordDialog()) {
            JOptionPane.showMessageDialog(
                null,
                "Access Denied! Incorrect Password.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(0); // Close the application
        }

        
    

        diaryManager = new DiaryManager();

        // Frame settings
        setTitle("Personal Diary Application");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Apply dark theme
        applyDarkTheme();

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Top panel (entry area)
        textArea = new JTextArea(10, 50);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setForeground(Color.WHITE);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel instructionsLabel = createStyledLabel("Write or Edit your diary entry:");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.add(instructionsLabel, BorderLayout.NORTH);
        topPanel.add(textScrollPane, BorderLayout.CENTER);

        // Side panel (entry list)
        entriesListModel = new DefaultListModel<>();
        entriesList = new JList<>(entriesListModel);
        entriesList.setFont(new Font("Arial", Font.PLAIN, 16));
        entriesList.setBackground(new Color(40, 40, 40));
        entriesList.setForeground(Color.WHITE);
        JScrollPane listScrollPane = new JScrollPane(entriesList);
        JLabel entriesLabel = createStyledLabel("Your Entries:");

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBackground(Color.BLACK);
        sidePanel.add(entriesLabel, BorderLayout.NORTH);
        sidePanel.add(listScrollPane, BorderLayout.CENTER);

        // Bottom panel (buttons)
        saveButton = createStyledButton("Add Entry", new Color(60, 179, 113));
        viewButton = createStyledButton("View Entry", new Color(70, 130, 180));
        deleteButton = createStyledButton("Delete Entry", new Color(220, 20, 60));
        renameButton = createStyledButton("Rename Entry", new Color(255, 165, 0));
        editButton = createStyledButton("Edit Entry", new Color(138, 43, 226));
        changeFolderButton = createStyledButton("Change or Open Folder", new Color(255, 215, 0));
        
        openFolderButton = createStyledButton("Open Folder", new Color(173, 216, 230));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.add(saveButton);
        bottomPanel.add(viewButton);
        bottomPanel.add(editButton);
        bottomPanel.add(renameButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(changeFolderButton);
        bottomPanel.add(openFolderButton);

        // Adding panels to main frame
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        // Action listeners
        saveButton.addActionListener(e -> addEntry());
        viewButton.addActionListener(e -> viewEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        renameButton.addActionListener(e -> renameEntry());
        editButton.addActionListener(e -> editEntry());
        changeFolderButton.addActionListener(e -> changeFolder());
        openFolderButton.addActionListener(e -> openFolder());
        changeFolderButton.setPreferredSize(new Dimension(170, 40));
        

        // Load existing entries
        loadEntries();

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                "Click 'Change or Open Folder' button to create or open a folder to save your diary entries.",
                "Folder Setup",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private void applyDarkTheme() {
        UIManager.put("control", new Color(45, 45, 45));
        UIManager.put("text", Color.WHITE);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Failed to apply Nimbus dark theme.");
        }
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setPreferredSize(new Dimension(102, 40));
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(10, 10, 5, 10));
        return label;
    }

    private void addEntry() {
        String content = textArea.getText().trim();
        if (!content.isEmpty()) {
            try {
                diaryManager.addEntry(content);
                entriesListModel.addElement("Entry " + diaryManager.getEntriesCount());
                JOptionPane.showMessageDialog(this, "Entry Added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                textArea.setText("");
                loadEntries();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cannot save an empty entry.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void changeFolder() {
        UIManager.put("FileChooser.background", new Color(30, 30, 30));
        UIManager.put("FileChooser.foreground", Color.RED);
        UIManager.put("FileChooser.listViewBackground", new Color(40, 40, 40)); 
        UIManager.put("FileChooser.listViewForeground", Color.GRAY);
        UIManager.put("FileChooser.control", new Color(45, 45, 45));
    
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        UIManager.put("chooser.foreground", Color.RED);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chooser.getSelectedFile();
            diaryManager.setFolder(selectedFolder.getAbsolutePath());
            loadEntries();
            JOptionPane.showMessageDialog(this, "Folder changed to: " + selectedFolder.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    

    private void openFolder() {
        
        JFileChooser chooser = new JFileChooser();
        
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = chooser.getSelectedFile();
            diaryManager.setFolder(selectedFolder.getAbsolutePath());
            loadEntries();
        }
    }

    

    private void viewEntry() {
        int selectedIndex = entriesList.getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                String entry = diaryManager.getEntry(selectedIndex);
    
                // Create a custom panel to hold the text area
                JTextArea textArea = new JTextArea(entry);
                textArea.setEditable(false);  // Make the text area non-editable
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setBackground(new Color(30, 30, 30));
                textArea.setForeground(Color.WHITE);
                textArea.setPreferredSize(new Dimension(500, 300)); // Custom size
    
                // Scroll the text area if the content is too long
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 300)); // Set size of the scroll pane
    
                // Show the dialog with the scroll pane
                JOptionPane.showMessageDialog(this, scrollPane, "View Entry", JOptionPane.INFORMATION_MESSAGE);
    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No entry selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    private void deleteEntry() {
        int selectedIndex = entriesList.getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                diaryManager.deleteEntry(selectedIndex);
                entriesListModel.remove(selectedIndex);
                JOptionPane.showMessageDialog(this, "Entry Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadEntries();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No entry selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadEntries() {
        entriesListModel.clear();
        try {
            ArrayList<String> entries = diaryManager.getAllEntries();
            for (String entryName : entries) {
                entriesListModel.addElement(entryName);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading entries.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renameEntry() {
        int selectedIndex = entriesList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String currentName = entriesListModel.get(selectedIndex);
            String newName = JOptionPane.showInputDialog(this, "Enter new name for the entry:", currentName);
            if (newName != null && !newName.trim().isEmpty()) {
                try {
                    diaryManager.renameEntry(selectedIndex, newName.trim());
                    entriesListModel.set(selectedIndex, newName.trim());
                    JOptionPane.showMessageDialog(this, "Entry renamed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {JOptionPane.showMessageDialog(this, "Error renaming entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid name provided.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "No entry selected.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
}

private void editEntry() {
    int selectedIndex = entriesList.getSelectedIndex();
    if (selectedIndex >= 0) {
        try {
            String entry = diaryManager.getEntry(selectedIndex);

            // Create a new JTextArea with a larger size
            JTextArea editTextArea = new JTextArea(15, 50);
            editTextArea.setText(entry);
            editTextArea.setLineWrap(true);
            editTextArea.setWrapStyleWord(true);
            editTextArea.setFont(new Font("Arial", Font.PLAIN, 16));

            // Set custom text and background colors
            editTextArea.setForeground(Color.WHITE); // Set text color
            editTextArea.setBackground(new Color(30, 30, 30)); // Set background color

            // Add JTextArea to a JScrollPane
            JScrollPane scrollPane = new JScrollPane(editTextArea);

            // Create a new JDialog
            JDialog editDialog = new JDialog(this, "Edit Entry", true);
            editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            editDialog.setLayout(new BorderLayout());
            editDialog.setSize(600, 400);
            editDialog.setLocationRelativeTo(this);

            // Create Save button for the dialog
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                String updatedContent = editTextArea.getText().trim();
                if (!updatedContent.isEmpty()) {
                    try {
                        // Update the entry
                        diaryManager.deleteEntry(selectedIndex);
                        diaryManager.addEntry(updatedContent);
                        loadEntries();
                        editDialog.dispose();
                        JOptionPane.showMessageDialog(this, "Entry updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving entry.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot save an empty entry.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            });

            // Add components to the dialog
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            editDialog.add(scrollPane, BorderLayout.CENTER);
            editDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Display the dialog
            editDialog.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading entry.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "No entry selected.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
}

//////////////////////////
private boolean showPasswordDialog() {
    JPasswordField passwordField = new JPasswordField(10);
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordField.setEchoChar('*'); // Masks the input

    Object[] dialogContent = {
        "Enter Password to Access Your Diary:", passwordField
    };

    int option = JOptionPane.showConfirmDialog(
        this,
        dialogContent,
        "Password Required",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    if (option == JOptionPane.OK_OPTION) {
        String enteredPassword = new String(passwordField.getPassword());
        String correctPassword = "rvitm"; // Replace with your predefined password

        return enteredPassword.equals(correctPassword);
    } else {
        return false; // If Cancel is pressed or dialog is closed
    }
}




public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new ModernDiaryApp().setVisible(true);
    });
}
}





class DiaryManager {
private Path folder;

public DiaryManager() {
    this.folder = Paths.get(System.getProperty("user.home"), "DiaryEntries");
    try {
        Files.createDirectories(folder);
    } catch (IOException e) {
        System.out.println("Failed to create directory for diary entries.");
    }
}

public void setFolder(String folderPath) {
    this.folder = Paths.get(folderPath);
    try {
        Files.createDirectories(folder);
    } catch (IOException e) {
        System.out.println("Failed to set new directory for diary entries.");
    }
}

public void addEntry(String content) throws IOException {
    String fileName = "Entry_" + System.currentTimeMillis() + ".txt";
    Path filePath = folder.resolve(fileName);
    Files.write(filePath, content.getBytes());
}

public void deleteEntry(int index) throws IOException {
    ArrayList<String> entries = getAllEntries();
    if (index >= 0 && index < entries.size()) {
        Path filePath = folder.resolve(entries.get(index));
        Files.deleteIfExists(filePath);
    }
}

public void renameEntry(int index, String newName) throws IOException {
    ArrayList<String> entries = getAllEntries();
    if (index >= 0 && index < entries.size()) {
        Path oldFilePath = folder.resolve(entries.get(index));
        Path newFilePath = folder.resolve(newName + ".txt");
        Files.move(oldFilePath, newFilePath);
    }
}

public void editEntry(int index, String newContent) throws IOException {
    ArrayList<String> entries = getAllEntries();
    if (index >= 0 && index < entries.size()) {
        Path filePath = folder.resolve(entries.get(index));
        Files.write(filePath, newContent.getBytes());
    }
}


public String getEntry(int index) throws IOException {
    ArrayList<String> entries = getAllEntries();
    if (index >= 0 && index < entries.size()) {
        Path filePath = folder.resolve(entries.get(index));
        return Files.readString(filePath);
    }
    return null;
}

public ArrayList<String> getAllEntries() {
    ArrayList<String> entries = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.txt")) {
        for (Path entry : stream) {
            entries.add(entry.getFileName().toString());
        }
    } catch (IOException e) {
        System.out.println("Failed to read entries.");
    }
    return entries;
}

public int getEntriesCount() {
    return getAllEntries().size();
}
}

