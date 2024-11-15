/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.passwordmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author davog
 */
public class MessageStorage {
    String key = "superDuperSecureSecretKey";
    SecretKey myDesKey;
    Cipher desCipher;

    
    public MessageStorage(){
        try
        {
            // Generating objects of KeyGenerator & SecretKey and setring up the encryption / decryption
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            myDesKey = keygenerator.generateKey();
            
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            myDesKey = keyFactory.generateSecret(desKeySpec);
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //Encript the given string using the DES key
    public String Encrypt(String message) {
        try
        {
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            // Encrypt the message
            byte[] encryptedData = desCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            // Encode the encrypted data to base64 to write to the file
            return Base64.getEncoder().encodeToString(encryptedData);
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    //Decript the given string using the DES key
    public String Decrypt(String cypher){
        try
        {
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
            // Decode the base64-encoded ciphertext
            byte[] encryptedData = Base64.getDecoder().decode(cypher);
            // Decrypt the data
            byte[] decryptedData = desCipher.doFinal(encryptedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    //Read all the messages from the text file and Decrypt them into an ArrayList<Message>
    public ArrayList<Message> ReadMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();
        
        try {
            File messageData = new File("MessageData.txt");
            Scanner myReader;
            myReader = new Scanner(messageData);
            //Read lines one by one and decrypt them
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = Decrypt(data);
                Message msg =Message.fromJson(data);
                System.out.println(msg.getContent());
                messages.add(msg);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }
        
        return messages;
    }
    
    //Save the given message to the text file after encripting it 
    public void SaveMessage(Message msg) {
        try {
            FileWriter myWriter = new FileWriter("MessageData.txt", true);
            myWriter.write(Encrypt(msg.toJson()));
            myWriter.write('\n');
            
            myWriter.flush();
            myWriter.close();
            

        } catch (IOException e) {
            System.out.println("Error occurred while writing to fie ");
            e.printStackTrace();
        }
    }
    
    //Create the text file if it doesnt exist 
    public void CreateFile() {
        try {
            File myObj = new File("MessageData.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating a new file.");
            e.printStackTrace();
        }
    }
    
    //Clear all the data on the file
    public void ClearFile() {
        try {
            FileWriter myWriter = new FileWriter("MessageData.txt", false);
            myWriter.write("");
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the file");
            e.printStackTrace();
        }
    }
}