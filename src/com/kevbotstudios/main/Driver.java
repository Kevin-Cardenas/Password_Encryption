package com.kevbotstudios.main;

import java.util.Scanner;

public class Driver {

    /**
     * repeatDecryption - attempts to make the decryption if a bad key is given
     * @param s - the scanner
     * @param serverSalt - the server salt to pad
     * @param encryptedPassword - password to be decrypted
     * @return the decrypted password
     */
    private static String repeatDecryption(Scanner s, String serverSalt, String encryptedPassword) {

        String decryptedPassword = "";

        // This is the part where we want to decrypt so we need the key the user gave
        System.out.println("Please re-enter the key you entered earlier");
        String key = s.next();

        key += serverSalt; // We need to make it of length 16

        try {

            // Decrypt the password
            decryptedPassword = Encryptor.decrypt(key, encryptedPassword);
            return decryptedPassword;
        } catch (Exception e) { // There are A LOT of exceptions that are being caught if the key isn't the right one

            return repeatDecryption(s, serverSalt, encryptedPassword);
        }
    }


    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        String key = "", password, encryptedPassword, decryptedPassword, serverSalt = "SALTY123"; // The server salt ensures a key of size 16

        // Ask user for a key of size 8 which we pad to ensure it is of size 16
        while (key.length() != 8) {

            System.out.println("Please enter a key of 8 characters");
            key = s.next();
        }

        key += serverSalt; // We need to make it of length 16

        System.out.println("Please enter a password to encrypt");
        password = s.next(); // The password can be of any length

        // Encrypt the password
        encryptedPassword = Encryptor.encrypt(key, password);

        System.out.println("Encrypted Password: " + encryptedPassword);

        // Decrypt the password
        decryptedPassword = repeatDecryption(s, serverSalt, encryptedPassword);

        System.out.println("Decrypted Password: " + decryptedPassword);

        s.close();
    }
}
