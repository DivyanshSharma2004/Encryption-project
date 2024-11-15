/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.passwordmanager;

import java.io.File;
import java.util.Scanner;
import javax.crypto.SecretKey;

/**
 *
 * @author marym
 */
public class PasswordManagerMain {

    public static void main(String[] args) {
        
  

        try {
            SecretKey secretKey;//= AESUtilPassMan.generateKey();
            
            Scanner input = new Scanner(System.in);

            System.out.println("PASSWORD MANAGER:");

            File f = new File("pwHash.txt");
            //If the master password is unset, set it now!
            if (!f.exists()) {
                PasswordManagement.getNewMasterPasswordAndSave(input, System.out);
            }

            String masterPw;
            while (true) {
                System.out.println("Enter master password:");
                masterPw = input.nextLine();
                if (PasswordManagement.verifyPasswordAgainstFile(masterPw)) {
                    System.out.println("Correct password! Unlocking...");
                    secretKey=PasswordManagement.getAESKeyFromPassword(masterPw, "hello");
                    break;
                }
                else
                {
                    System.out.println("Incorrect password!");
                }
            }
            
            PasswordManagement passManager = new PasswordManagement(secretKey);

            while (true) {
                System.out.println("Enter options:\n1. Add Password\n2. Get Password\n3. Delete Password \n4. Exit");

                String ans = input.nextLine();

                switch (ans) {
                    case "1":
                        System.out.println("Enter website: ");
                        String website = input.nextLine();
                        System.out.println("Enter password: ");
                        String pass = input.nextLine();
                        passManager.addPass(website, pass, input); // add password
                        break;
                    case "2":
                        System.out.println("Enter website: ");
                        website = input.nextLine();
                        String myPass = passManager.getPass(website); // gets password and key
                        System.out.println("Website: " + website + " Password: " + myPass);
                        break;
                    case "3":
                        System.out.println("Enter website: ");
                        website = input.nextLine();
                        passManager.deletePass(website); // deletes password
                        break;
                    case "4":
                        System.out.println("Thank you! Goodbye!");
                        return;
                        
                    default:
                        System.out.println("Please, enter one of the following: 1. add, 2. retrieve, 3. delete, 4. exit");
                }

            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
