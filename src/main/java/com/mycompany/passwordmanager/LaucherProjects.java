///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.passwordmanager;
//
//import java.util.Scanner;
//
///**
// *
// * @author marym
// */
//public class LaucherProjects {
//
//    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);  // Create Scanner once
//
//        try {
//            boolean running = true;
//            System.out.println("Welcome to the Application!");
//            while (running) {
//                System.out.println("Please choose an option:");
//                System.out.println("1. Password Manager");
//                System.out.println("2. Secure Messaging");
//                System.out.println("3. Exit");
//
//                // Get the user's choice
//                String choice = input.nextLine().trim();
//
//                switch (choice) {
//                    case "1":
//                        // Launch the password manager
//                        launchPasswordManager(input);
//                        break;
//                    case "2":
//                        // Launch the other feature
//                        launchOtherFeature(input);
//                        break;
//                    case "3":
//                        // Exit the program
//                        System.out.println("Exiting the application. Goodbye!");
//                        running = false; // Set running to false to exit the loop
//                        break;
//                    default:
//                        System.out.println("Invalid option. Please enter 1, 2, or 3.");
//                }
//            }
//        } finally {
//            input.close();  // Close the scanner once when program ends
//        }
//    }
//
//    // Method to launch the Password Manager
//    private static void launchPasswordManager(Scanner input) {
//        try {
//           // PasswordManagerMain.startPasswordManager(input);  // Call the custom method
//        } catch (Exception e) {
//            System.out.println("Error launching Password Manager: " + e);
//        }
//    }
//
//    // Method to launch the other feature
//    private static void launchOtherFeature(Scanner input) {
//        try {
//           // PasswordManagerMain.startPasswordManager(input);
//        } catch (Exception e) {
//            System.out.println("Error launching other feature: " + e);
//        }
//    }
//}
