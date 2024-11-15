package com.mycompany.passwordmanager;

/**
 *
 * @author divme
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.*;

public class FileIntegrityChecker {

    // List of supported hashing algorithms
    private static final List<String> algorithmns = Arrays.asList("SHA-256", "SHA-512", "MD5");
    
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Loading...");
        System.out.println("File loaded succesfully");
        System.out.println("Choose an option:");
        System.out.println("1. Generate File Hash");
        System.out.println("2. Verify File Integrity");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character

        try {
            System.out.println("Available Algorithms: " + algorithmns);
            System.out.print("Which algorithm do you want to use? ");
            String algorithm = scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter the file path: ");
                String filePath = scanner.nextLine();
                String hash = generateFileHash(filePath, algorithm);
                System.out.println("File Hash: " + hash);

                System.out.print("Do you want to save this hash to a file? (yes/no): ");
                if ("yes".equalsIgnoreCase(scanner.nextLine())) {
                    System.out.print("Enter the output file path: ");
                    String outputFile = scanner.nextLine();
                    saveHashToFile(filePath, hash, outputFile);
                    System.out.println("Hash saved to file succesfully.");
                }
            } else if (choice == 2) {
                System.out.print("Enter the file path: ");
                String filePath = scanner.nextLine();
                System.out.print("Enter the expected hash: ");
                String expectedHash = scanner.nextLine();
                boolean isMatch = verifyFileIntegrity(filePath, expectedHash, algorithm);
                if(isMatch){
                    System.out.println("File is intact.");
                }else{
                    System.out.println("File is not intact.");
                }
                
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
    // This method generates the hash of a file based on the chosen algorithm
    public static String generateFileHash(String filePath, String algorithm) throws IOException, NoSuchAlgorithmException {
        if (!algorithmns.contains(algorithm)) {
            throw new NoSuchAlgorithmException(algorithm + " is not supported.");
        }

            // Get the digest object for the selected algorithm
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            // Read the entire file into a byte array
            byte[] fileBytes = Files.readAllBytes(Path.of(filePath));

            // Update the digest with the file bytes
            digest.update(fileBytes);

            // Convert the hash bytes to a hex string
            StringBuilder hashString = new StringBuilder();
            for (byte b : digest.digest()) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        }

    // This method checks if a file's hash matches the expected hash
    public static boolean verifyFileIntegrity(String filePath, String expectedHash, String algorithm) throws IOException, NoSuchAlgorithmException {
        String fileHash = generateFileHash(filePath, algorithm);
        return fileHash.equalsIgnoreCase(expectedHash);
    }

    // This saves the hash of a file to a specified file
    public static void saveHashToFile(String filePath, String hash, String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            writer.write(hash);
        }
    }
}
