package com.easy.interviewsecurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AesCbcUtil {

    private static final String ALGORITHM = "AES";
    // 模式CBC，填充PKCS5Padding
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * AES CBC加密
     * @param content 明文
     * @param key 密钥 16字节(AES‑128) /32字节(AES‑256)
     * @param iv  偏移向量，固定16字节
     * @return base64密文
     * @throws Exception e
     */
    public static String encrypt(String content, String key, String iv) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * AES CBC解密
     */
    public static String decrypt(String base64CipherText, String key, String iv) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decodeBytes = Base64.getDecoder().decode(base64CipherText);
        byte[] origin = cipher.doFinal(decodeBytes);
        return new String(origin, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        // AES‑128：key必须16位字符；iv必须16位字符
        String plainText = "hello aes java测试数据123";
        String key = "1234567890abcdef"; // 16字节 AES‑128  // key 不要硬编码代码
        String iv  = "abcdef1234567890"; // 固定16字节

        String cipher = encrypt(plainText, key, iv);
        System.out.println("密文(Base64): " + cipher);

        String origin = decrypt(cipher, key, iv);
        System.out.println("解密明文: " + origin);
    }
}

