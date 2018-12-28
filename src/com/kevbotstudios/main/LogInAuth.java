package com.kevbotstudios.main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * LogInAuth - this class deals with the logic of utilizing the MD5 encryption method to hash and salt the password the
 *           user puts during registration for the master key. We cannot go backwards after the salt and hash are created
 *           WE MUST store the salt on the server that was used at the time of the user registering.
 * @author Kevin Cardenas
 */
public class LogInAuth {

    private Map<String, String> passwordMap; // Password -> MD5 Password + Salt
    private Map<String, byte[]> saltMap; // Password -> Salt

    /** LogInAuth - this is the constructor */
    public LogInAuth() {

        passwordMap = new HashMap<>();
        saltMap = new HashMap<>();
    }

    /**
     * generateMD5Password - this generates the MD5 password based on the salt and password at the time of registration.
     *                     The idea is that when the user signs in after initial registration we run the password through
     *                     this function again to make sure they are the same. The original salt that was used at registration
     *                     must be used to verify validity
     * @param password - the plaintext password the user enters
     * @param salt - the salt being used. Generated on registration
     * @return a String of the hashed and salted password
     */
    public String generateMD5Password(String password, byte[] salt) {

        String hashedPassword = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(salt); // Update the message digest with the salt

            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {

                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            hashedPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        passwordMap.put(password, hashedPassword); // To check validity
        saltMap.put(password, salt); // WE MUST STORE THE SALT

        return hashedPassword;
    }

    /**
     * generateSalt - generates salt using the SecureRandom objects
     * @return the salt as a byte[]
     */
    public byte[] generateSalt() {

        byte[] salt = new byte[16];

        try {

            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG", "SUN");

            rand.nextBytes(salt);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return salt;
    }

    /**
     * verifyLogIn - verifies the log in after running through the generateMD5Password function
     * @param password - the plaintext password we are trying to access
     * @return true if the hashed passwords are identical. Otherwise return false
     */
    public boolean verifyLogIn(String password) {

        boolean identical;
        byte[] originalSaltUsed = saltMap.get(password);
        String encryptedPassword = generateMD5Password(password, originalSaltUsed);
        String storedPassword = passwordMap.get(password);

        identical = storedPassword.equals(encryptedPassword);

        System.out.println("Encrypted Password: " + encryptedPassword);
        System.out.println("Encrypted Password Stored On The Server: " + storedPassword);
        System.out.println("The Two Passwords Are The Same: " + identical);

        return identical;
    }
}
