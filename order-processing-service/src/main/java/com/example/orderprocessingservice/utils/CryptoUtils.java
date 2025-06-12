package com.example.orderprocessingservice.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    // This will be moved to GitHub later or Env + .gitIgnore
    private static final String SECRET = "-zygPPN#upvFBRJ{MHG&N_Lg2XE!rK0V";

    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
