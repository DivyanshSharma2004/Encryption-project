/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.passwordmanager;

//summary
//you enter a password; it creates 2 things from this password:
//1: hash that is compared to one stored in a file
//2: AES key that can be used to encrypt/decrypt (using the code you already have)
//So if you enter a wrong password, it kicks you out because the hash doesnt match! If you enter a correct one, 
//it lets you in and you use this password to create an AES key that you then use for encryption/decryption
//the manager has a file with a hashed master password
//1) input master password and create the hashed master password file if the hashed master password file doesnt exists; 
//2)when the user enters the master password, you generate 2 keys using getKeyFromPassword with 2 different salts:  https://www.baeldung.com/java-aes-encryption-decryption ; only did one salt in the end ;
//first one is for hashing and login stuff... so you hash it and compare it with the hash file; if they dont match, you exit the app; second one is your encrypt/decrypt AES key;
//you can use it for the encryption/decryption of your passwords!
//3) menu: change master password, show password for website, delete password... now you have your AES decryption key, woo!
//prompt user for a password
//TODO: get new master  password decrypt all passwords encypt all passwords with new aes than you tell user the master password is changed
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author marym
 */
public class PasswordManagement {

    private final Map<String, String> passwordStore; // stores the website passwords encreypted in key val format
    private final SecretKey secretKey; // aes encryption key
    private final File storageFile; // file for storing encrypted password

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt()); // hashes password 
    }

    // hashes master password and saves it to pwHash.txt
    public static void hashPasswordSaveToFile(String password) {
        String pwHash = hashPassword(password);
        File f = new File("pwHash.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(pwHash);
            bw.newLine();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public static void getNewMasterPasswordAndSave(Scanner in, PrintStream out) {
        out.println("Enter a new master password!"); //prompts user to add new master password
        String pw = "";
        do {
            pw = in.nextLine();

        } while (pw.length() < 1); // checks password isnt empty
        hashPasswordSaveToFile(pw);

    }
    
    
    //   //TODO: prompt user to enter a better password if this one is too bad
//
//        String password = "";
//        String strength = "";
//        while (true) {
//            password = input.nextLine();
//
//            // Check the strength of the entered master password
//            strength = passStrength(password);
//
//            // If the password is weak, ask the user if they want to improve it
//            if (strength.startsWith("weak")) {
//                out.println(strength);
//
//                // While loop to repeatedly ask if they want to save a weak password
//                while (true) {
//                    out.println("do you want to improve the password? (yes/no)");
//
//                    String response = input.nextLine().toLowerCase();
//
//                    if (response.equals("yes")) {
//                        out.println("enter a stronger password:");
//                        password = input.nextLine();
//
//                        strength = passStrength(password);
//
//                        if (strength.startsWith("weak")) {
//                            out.println("new password is still a " + strength);
//                        } else {
//                            out.println("new password is strong. Saving password");
//                            break;
//                        }
//                    } else if (response.equals("no")) {
//                        out.println("saving weak password");
//                        break;
//                    } else {
//                        out.println("invalid please type 'yes' or 'no'.");
//                    }
//                }
//
//            } else {
//                out.println("Saving master password");
//                break;
//            }
//
//        }

    // verifies the input password against the saved hashed password in the file
    public static boolean verifyPasswordAgainstFile(String password) {
        File f = new File("pwHash.txt");
        try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
            String hash = bf.readLine();
            return verifyPassword(password, hash); // checks if passwords match

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return false;
    }

    // compares hashes against each other
    public static boolean verifyPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    //https://www.baeldung.com/java-aes-encryption-decryption
    // salt used to prevent rainbow table attacks   
    public static SecretKey getAESKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);  // uses password and salt to generate AES
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES"); // generates aes key 
        return secret;
    }

    public PasswordManagement(SecretKey secretKey) { // initialises passwordStore, secretKey, and storageFile
        this.passwordStore = new HashMap<>();
        this.secretKey = secretKey;
        this.storageFile = new File("Passwords.txt");
        if (storageFile.exists()) { // checks if file exists
            loadPass();
        } else {
            try {
                storageFile.createNewFile();  // Create the file if it doesn't exist
            } catch (Exception e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }

    // add password 
    public void addPass(String website, String password, Scanner input) throws Exception {

        // Check the strength of the password
        String strength = passStrength(password);

        // If the password is weak, ask the user if they want to improve it
        if (strength.startsWith("weak")) {
            System.out.println(strength);

            // While loop to repeatedly ask if they want to save a weak password
            while (true) {
                System.out.println("do you want to improve the password? (yes/no)");

                String response = input.nextLine().toLowerCase();

                if (response.equals("yes")) {
                    System.out.println("enter a stronger password:");
                    password = input.nextLine();

                    strength = passStrength(password);

                    if (strength.startsWith("weak")) {
                        System.out.println("new password is still a " + strength);
                    } else {
                        System.out.println("new password is strong. Saving password");
                        break;
                    }
                } else if (response.equals("no")) {
                    System.out.println("saving weak password");
                    break;
                } else {
                    System.out.println("invalid please type 'yes' or 'no'.");
                }
            }
        }
        // Encrypt and store the password
        String encryptedPassword = AESUtilPassMan.encrypt(password, secretKey);
        passwordStore.put(website, encryptedPassword);
        savePass();
        System.out.println("Password saved for: " + website);
    }

    private static String passStrength(String password) {
        boolean uppercase = false;
        boolean lowercase = false;
        boolean numbers = false;
        boolean characters = false;
        String characterString = "!@£$%^&?,./*()_:|<>#";

        if (password.length() < 8) {
            return "weak password! Your password must be at lest 8 characters long";
        }

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) { // checks for uppercase
                uppercase = true;
            } else if (Character.isLowerCase(c)) { // checks for lowercase
                lowercase = true;
            } else if (Character.isDigit(c)) { // checks for digit
                numbers = true;
            } else if (characterString.indexOf(c) >= 0) { // checks for special character
                characters = true;
            }
        }

        if (!uppercase) {
            return "weak password: must have at least one uppercase";
        }
        if (!lowercase) {
            return "weak password: must have at least one lowercase";
        }
        if (!numbers) {
            return "weak password: must have at least one number";
        }
        if (!characters) {
            return "weak password: must have at least one special character";
        }

        return "Strong password";

    }

    public String getPass(String website) throws Exception {
        if (!passwordStore.containsKey(website)) { // if website not there returns message
            return "Sorry inccorect password or website please try again";
        }
        String encryptedPass = passwordStore.get(website); // gets password
        String pass = AESUtilPassMan.decrypt(encryptedPass, secretKey); // decrypts password with key
        return pass;
    }

    public void deletePass(String website) {
        if (passwordStore.remove(website) != null) { // remvoes passwords based of website name
            savePass();
            System.out.println("Password deleted for " + website);
        } else {
            System.out.println("Sorry inccorect password or website please try again");
        }
    }

    private void savePass() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storageFile))) { // buffer writer to write to file
            for (Map.Entry<String, String> entry : passwordStore.entrySet()) { // saves website and encypted password
                bw.write(entry.getKey() + ":" + entry.getValue()); // website : password encypted
                bw.newLine(); // new line after each entry
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void loadPass() {
        try (BufferedReader bf = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = bf.readLine()) != null) { // reads line till none
                String[] parts = line.split(":"); // sperates website as key and password as value
                if (parts.length == 2) {
                    passwordStore.put(parts[0], parts[1]); // password stored
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}
