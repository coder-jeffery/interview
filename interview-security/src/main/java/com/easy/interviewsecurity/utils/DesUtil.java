package com.easy.interviewsecurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * DES 已不安全，仅遗留系统对接使用，新项目禁止！
 * key: 必须8字节；IV：CBC模式必须8字节
 */
public class DesUtil {
    private static final String DES_TRANS = "DES/CBC/PKCS5Padding";

    /**
     * DES加密，随机IV，输出 base64(IV + 密文)
     * @param content 明文
     * @param keyBytes 8字节密钥
     * @return base64字符串
     */
    public static String encrypt(String content, byte[] keyBytes) throws Exception {
        if (keyBytes.length != 8) {
            throw new IllegalArgumentException("DES key must be 8 bytes");
        }
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(DES_TRANS);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // iv(8) + cipher
        byte[] combined = new byte[8 + cipherBytes.length];
        System.arraycopy(iv, 0, combined, 0, 8);
        System.arraycopy(cipherBytes, 0, combined, 8, cipherBytes.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String base64Str, byte[] keyBytes) throws Exception {
        if (keyBytes.length != 8) {
            throw new IllegalArgumentException("DES key must be 8 bytes");
        }
        byte[] all = Base64.getDecoder().decode(base64Str);
        byte[] iv = new byte[8];
        byte[] cipherBytes = new byte[all.length - 8];
        System.arraycopy(all, 0, iv, 0, 8);
        System.arraycopy(all, 8, cipherBytes, 0, cipherBytes.length);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(DES_TRANS);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plain = cipher.doFinal(cipherBytes);
        return new String(plain, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        byte[] desKey8 = "12345678".getBytes(StandardCharsets.UTF_8); // 必须8字节
        String msg = "DES测试数据";
        String enc = encrypt(msg, desKey8);
        System.out.println("DES密文：" + enc);
        String dec = decrypt(enc, desKey8);
        System.out.println("DES解密：" + dec);
    }
}

