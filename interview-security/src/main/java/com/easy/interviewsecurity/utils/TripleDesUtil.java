package com.easy.interviewsecurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 3DES DESede，多用于老银行、旧接口对接
 * key：标准24字节；IV：8字节
 */
public class TripleDesUtil {
    private static final String DESEDE_TRANS = "DESede/CBC/PKCS5Padding";

    /**
     * 3DES加密，随机IV，输出base64(IV + cipherBytes)
     * @param content 明文
     * @param key24Bytes 24字节密钥
     * @return base64密文
     */
    public static String encrypt3Des(String content, byte[] key24Bytes) throws Exception {
        if (key24Bytes.length != 24) {
            throw new IllegalArgumentException("3DES(DESede) key must be 24 bytes");
        }
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec keySpec = new SecretKeySpec(key24Bytes, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(DESEDE_TRANS);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        byte[] combined = new byte[8 + cipherBytes.length];
        System.arraycopy(iv, 0, combined, 0, 8);
        System.arraycopy(cipherBytes, 0, combined, 8, cipherBytes.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt3Des(String base64Str, byte[] key24Bytes) throws Exception {
        if (key24Bytes.length != 24) {
            throw new IllegalArgumentException("3DES(DESede) key must be 24 bytes");
        }
        byte[] all = Base64.getDecoder().decode(base64Str);
        byte[] iv = new byte[8];
        byte[] cipherBytes = new byte[all.length - 8];
        System.arraycopy(all, 0, iv, 0, 8);
        System.arraycopy(all, 8, cipherBytes, 0, cipherBytes.length);

        SecretKeySpec keySpec = new SecretKeySpec(key24Bytes, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(DESEDE_TRANS);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plain = cipher.doFinal(cipherBytes);
        return new String(plain, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        // 24字节示例密钥，3组8字节拼接
        byte[] key24 = "12345678abcdefgh87654321".getBytes(StandardCharsets.UTF_8);
        System.out.println("3DES密钥长度：" + key24.length);

        String text = "3DES测试，老系统对接";
        String enc = encrypt3Des(text, key24);
        System.out.println("3DES密文：" + enc);
        String dec = decrypt3Des(enc, key24);
        System.out.println("3DES解密：" + dec);
    }
}
