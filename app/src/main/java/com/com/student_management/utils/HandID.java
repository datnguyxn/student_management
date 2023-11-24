package com.com.student_management.utils;

import com.com.student_management.constants.App;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HandID {
    private static String key = "0123456789abcdef";
    private static String iv = "fedcba9876543210";
    private static final SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
    private static final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
    public static String encrypt(String plainText) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = encryptCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = decryptCipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
