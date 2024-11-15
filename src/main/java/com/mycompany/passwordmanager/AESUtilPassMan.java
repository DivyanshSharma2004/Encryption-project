/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.passwordmanager;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author marym
 */
public class AESUtilPassMan {

    private static final String ALGORITHM = "AES";

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES"); // key generator of AES
        kg.init(256); // key size of bits 256 
        return kg.generateKey(); 
    }

    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM); // create cipher instance for algorithm
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); // initialize the cipher in encryption mode with the secret key
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes()); // encrypt to plain text
        return Base64.getEncoder().encodeToString(encryptedBytes); //  encode bytes to Base64 stirng
    }

    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);  // create cipher instance for algorithm
       cipher.init(Cipher.DECRYPT_MODE, secretKey); // initialise the cipher in decryption mode with the secret key
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText); // decode base64 string to bytes
        byte[] decryptedBytes = cipher.doFinal(decodedBytes); // decrypt the bytes
        return new String(decryptedBytes); // changes bytes to string
    }

    public static String keyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded()); // convert key to a base64 String
    }

    public static SecretKey stringToKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString); // decode base 64 string to bytes
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM); // create secret key  from decode bytes
    }

}
