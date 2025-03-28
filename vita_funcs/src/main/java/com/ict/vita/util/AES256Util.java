package com.ict.vita.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256Util {

    private static final String SECRET_KEY = "01234567890123456789012345678901";
    private static final String INIT_VECTOR = "0123456789012345";

    public static String encrypt(String value) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decoded = Base64.getDecoder().decode(encryptedValue);
        return new String(cipher.doFinal(decoded));
    }
}
