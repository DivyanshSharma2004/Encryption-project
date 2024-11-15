/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.passwordmanager;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author marym
 */



// summary
// Program loads a list of programs from a JSON file
// You are than presented with a menu to choose a program and then executes 
// When program is clicked it than calls the java class; the jar file
// You can choose something thats not there and it will tell you its and invalid option
// The program gives you an option to exit at the end 


public class Launcher {

    // loads programs from json file
    public static ArrayList<ProgramEntry> loadProgramList() {
        Gson gson = new Gson(); // Create Gson object for JSON pasring
        try (Reader reader = new FileReader("programsList.json")) {
            // Define the Type so we can parse the json into an arraylist of ProgramEntry objects
            Type programListType = new TypeToken<ArrayList<ProgramEntry>>() {
            }.getType(); // Parse JSON file and return list of ProgramEntey objects
            return gson.fromJson(reader, programListType); // Parse the JSON to a list of ProgramEntry objects
        } catch (Exception e) {
            System.out.println("Failed to load programs: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        ArrayList<ProgramEntry> theList = loadProgramList(); // Load list of programs from json
        if (theList == null || theList.isEmpty()) { // If file empty or null exits
            System.out.println("Exiting!");
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter selection:");
            System.out.println("0: (exit)");
            for (int i = 0; i < theList.size(); i++) { // Displays list of programs for user to chose from
                System.out.println(i + 1 + ": " + theList.get(i).description);
            }
            int choice = scanner.nextInt(); // Get users choice
            scanner.nextLine(); // Eat newline
            if (choice < 0 || choice > theList.size()) {
                System.out.println("Invalid choice!");
                continue;
            }
            if (choice == 0) {
                System.out.println("Exiting!");
                System.exit(0);
            }

            // Get the selected program based on choice
            ProgramEntry selectedProgram = theList.get(choice - 1);

            // Execute the selected program using class name
            launchProgram(selectedProgram.className);
        }

    }

    private static void launchProgram(String progName)
    {
        String args[]={};
        switch(progName)
        {
            
            case "PWMNG":
                PasswordManagerMain.main(args);
                break;
        }
    }
    private static void executeProgram(String className) {

        try {
            File f = new File(className); // Create a file object for class name
            if (!f.exists()) {
                System.out.println("File does not exist!");
                return;
            }

            // Process Builder to run Java using netbeans JDK
            ProcessBuilder processBuilder = new ProcessBuilder("\"C:\\Program Files\\Apache NetBeans\\jdk\\bin\\java.exe\"",  "-jar",f.getAbsolutePath());

            processBuilder.inheritIO(); // Inherit I/O to show program output 
            Process process = processBuilder.start(); // Start process
            process.waitFor(); // Wait for the process to complete

        } catch (Exception e) {
            System.out.println("Failed to execute program: " + e.getMessage());
        }
    }
}
