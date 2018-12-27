package com.kevbotstudios.main;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encryptor - this class deals with the encryption and decryption of the password
 * @author Kevin Cardenas
 */
public class Encryptor {

    private static final String ALGORITHM = "AES"; // The algorithm for encryption we are using. Requires key of size of multiple 16

    /**
     * encrypt - encrypts the given password
     * @param key - the key used to encrypt the password
     * @param password - the password to be encrypted with AES
     * @return the encrypted version of the text
     */
    public static String encrypt(String key, String password) {

        String encryptedPassword = "";

        try {

            SecretKeySpec secKey = new SecretKeySpec(key.getBytes(), ALGORITHM); // Secret Key object
            Cipher cipher = Cipher.getInstance(ALGORITHM); // The AES Cipher

            cipher.init(Cipher.ENCRYPT_MODE, secKey);

            byte[] encrypted = cipher.doFinal(password.getBytes()); // The encrypted password

            encryptedPassword = new BASE64Encoder().encode(encrypted); // Padding the encryption text to be base 16

        } catch (Exception e) {

            e.printStackTrace();
        }

        return encryptedPassword;
    }

    /**
     * decrypt - decrypts the encrypted password based on the key that is given to encrypt the password
     * @param key - the key the user gave to encrypt the password
     * @param encryptedPassword - the password which needs to be decrypted
     * @return the decrypted password
     * @throws Exception which will be handled by the caller until the key given for decryption is good
     */
    public static String decrypt(String key, String encryptedPassword) throws Exception {

        String decryptedPassword = "";
        SecretKeySpec secKey = new SecretKeySpec(key.getBytes(), ALGORITHM); // The secret key
        Cipher cipher = Cipher.getInstance(ALGORITHM); // Using the AES Cipher

        cipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] encodedEncryption = new BASE64Decoder().decodeBuffer(encryptedPassword); // Decoding back to base 16
        byte[] decrypted = cipher.doFinal(encodedEncryption);

        decryptedPassword = new String(decrypted);

        return decryptedPassword;
    }
}
