/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.passwordmanager;

/**
 *
 * @author davog
 */
import java.util.*;
import javax.swing.JOptionPane;

public class ChatServer {
    public static void main(String[] args) {
        boolean exit = false;
        ArrayList<Message> messages;
         
        MessageStorage storage = new MessageStorage();
        storage.CreateFile();
        messages = storage.ReadMessages();
        
        while(!exit){
            String message = JOptionPane.showInputDialog( "Enter the message that you would like to send. Enter Exit to exit, Enter Clear to clear the storage");
            
            if(message.equalsIgnoreCase("EXIT")){
                exit = true;
                System.out.println("Exiting ...");
            }
            else if(message.equalsIgnoreCase("CLEAR")){
                storage.ClearFile();
                System.out.println("Cleared the storage");
            }
            else{
                Message msg = new Message("Sender", "Reciever", message, "message");
                storage.SaveMessage(msg);
                System.out.println(message);
                messages.add(msg);
            }
        }

        
        
        
        //storage.ClearFile();
        
    }
}
