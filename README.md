Project Name:- Personal Diary Application

Team:- 
V.Haricharanreddy,USN:-1RF23IS091 
Vrushabh.KB,USN:- 1RF23IS093

Features:-
Password Authentication: Secure access to the diary with a password.
Add Entry: Allows users to add new diary entries.
View Entry: View the contents of a selected diary entry.
Edit Entry: Edit an existing diary entry.
Delete Entry: Delete a selected diary entry.
Rename Entry: Rename a selected diary entry.
Change Folder: Change the directory where diary entries are stored.
Open Folder: Open the current folder where entries are stored.


Requirements:-
Java: The application is built using Java Swing for the GUI, JAVA NIO for file handling
JDK 8 or later: Required to run the application.
IDE: Any Java IDE (like IntelliJ IDEA or Eclipse) or a text editor (like VS Code) with Java support.

Usage:-
Run the application by executing the ModernDiaryApp class.
Enter the password ("rvitm" by default) to access your diary.
After entering the password you can see the App interface where you can create diaries and perform other operations on it but you cant save the diaries to the system memory.
In order to save the diaries into the system memory first you need to create or select the existing folder to save your diaries in the respective folder using "Select or Open folder" button.
After creating or opening the folder where you want to store your diaries, You can follow the these steps :-
Write or edit a diary entry in the main text area.
Click the "Add Entry" button to save the diary entry.
Select an entry from the list and click "View Entry" to view its content.
Use the "Edit Entry" button to modify a selected entry.
Rename or Delete entries using the corresponding buttons.
Change or open the folder where the entries are stored using the respective buttons.


Code Overview:-
ModernDiaryApp.java
This is the main class that contains the GUI for the diary application. 
It provides a text area for writing and editing entries, a list of existing entries, and buttons for adding, viewing, editing, renaming, and deleting entries.

DiaryManager.java
This class handles file operations such as saving, loading, renaming, and deleting diary entries. 
It uses Java NIO (Path, Files) for file handling and stores entries as .txt files.
