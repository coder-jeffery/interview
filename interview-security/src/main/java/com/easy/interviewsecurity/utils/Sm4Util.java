package com.easy.interviewsecurity.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.SecureRandom;
import java.util.Base64;

public class Sm4Util {

    static {
        // 注册BC提供者
        if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private static final String TRANSFORMATION = "SM4/CBC/PKCS5Padding";

    /**
     * SM4加密，随机IV；输出 base64(IV + 密文)
     * @param content 明文
     * @param key16 16字节密钥
     * @return base64
     */
    public static String encrypt(String content, byte[] key16) throws Exception {
        if(key16.length != 16){
            throw new IllegalArgumentException("SM4 key must be 16 bytes");
        }
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec keySpec = new SecretKeySpec(key16, "SM4");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // iv + cipher
        byte[] combined = new byte[iv.length + cipherBytes.length];
        System.arraycopy(iv,0,combined,0,16);
        System.arraycopy(cipherBytes,0,combined,16,cipherBytes.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String base64Str, byte[] key16) throws Exception {
        if(key16.length !=16){
            throw new IllegalArgumentException("SM4 key must be 16 bytes");
        }
        byte[] all = Base64.getDecoder().decode(base64Str);
        byte[] iv = new byte[16];
        byte[] cipherBytes = new byte[all.length -16];
        System.arraycopy(all,0,iv,0,16);
        System.arraycopy(all,16,cipherBytes,0,cipherBytes.length);

        SecretKeySpec keySpec = new SecretKeySpec(key16, "SM4");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plain = cipher.doFinal(cipherBytes);
        return new String(plain, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        byte[] sm4Key = "12345678abcdefgh".getBytes(StandardCharsets.UTF_8); //16字节
        String msg = "国密SM4测试中文";
        String enc = encrypt(msg, sm4Key);
        System.out.println("SM4密文："+enc);
        String dec = decrypt(enc, sm4Key);
        System.out.println("SM4解密："+dec);
    }
}

