package com.easy.interviewsecurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Aes256CbcUtil {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * AES‑256 CBC加密
     * @param content 明文
     * @param keyBytes 密钥字节数组，必须32字节！
     * @return base64(IV(16字节) + 密文字节)
     */
    public static String encrypt(String content, byte[] keyBytes) throws Exception {
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("AES‑256 key must be 32 bytes");
        }
        // 随机生成16字节IV
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // iv + cipher 拼接
        byte[] combined = new byte[ivBytes.length + cipherBytes.length];
        System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
        System.arraycopy(cipherBytes, 0, combined, ivBytes.length, cipherBytes.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    /**
     * AES‑256 CBC解密
     * @param base64Str base64(IV+密文)
     * @param keyBytes 32字节密钥
     * @return 明文
     */
    public static String decrypt(String base64Str, byte[] keyBytes) throws Exception {
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("AES‑256 key must be 32 bytes");
        }
        byte[] allBytes = Base64.getDecoder().decode(base64Str);
        byte[] ivBytes = new byte[16];
        byte[] cipherBytes = new byte[allBytes.length - 16];
        System.arraycopy(allBytes, 0, ivBytes, 0, 16);
        System.arraycopy(allBytes, 16, cipherBytes, 0, cipherBytes.length);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        // AES‑256：密钥严格32字节。示例：32个字符
        String keyRaw = "abcdefghijklmnopabcdefghijklmnop";
        byte[] key32 = keyRaw.getBytes(StandardCharsets.UTF_8);
        System.out.println("key字节长度:" + key32.length);

        String plainText = "AES‑256测试，中文123！";
        String cipherText = encrypt(plainText, key32);
        System.out.println("密文Base64：" + cipherText);

        String origin = decrypt(cipherText, key32);
        System.out.println("解密明文：" + origin);
    }
}

