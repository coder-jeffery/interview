package com.easy.interviewsecurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AesGcmUtil {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // tag位
    private static final int NONCE_LEN = 12;       // nonce推荐12字节

    public static String encrypt(String content, String key) throws Exception {
        byte[] keyByte = key.getBytes(StandardCharsets.UTF_8);
        byte[] nonce = new byte[NONCE_LEN];
        new SecureRandom().nextBytes(nonce);

        SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        byte[] cipherText = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // nonce + cipher
        byte[] out = new byte[nonce.length + cipherText.length];
        System.arraycopy(nonce,0,out,0,nonce.length);
        System.arraycopy(cipherText,0,out,nonce.length,cipherText.length);
        return Base64.getEncoder().encodeToString(out);
    }

    public static String decrypt(String base64Data, String key) throws Exception {
        byte[] all = Base64.getDecoder().decode(base64Data);
        byte[] nonce = new byte[NONCE_LEN];
        byte[] cipherBytes = new byte[all.length - NONCE_LEN];
        System.arraycopy(all,0,nonce,0,NONCE_LEN);
        System.arraycopy(all,NONCE_LEN,cipherBytes,0,cipherBytes.length);

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
        byte[] plain = cipher.doFinal(cipherBytes);
        return new String(plain, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        String key = "1234567890abcdef";
        String s = "gcm加密测试";
        String e = encrypt(s,key);
        System.out.println(e);
        System.out.println(decrypt(e,key));
    }
}
