package com.easy.interviewsecurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtils {

    /**
     * 生成 RSA‑2048 密钥对
     * @return KeyPair 公钥、私钥
     */
    public static KeyPair generateRsaKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(keySize, new SecureRandom());
        return keyPairGen.generateKeyPair();
    }

    /**
     * 公钥加密 OAEP 安全填充（推荐）
     * @param plainText 明文
     * @param publicKey 公钥
     * @return base64密文
     */
    public static String encryptByPublicKey(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepSpec = new OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT
        );
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 私钥解密 OAEP
     */
    public static String decryptByPrivateKey(String base64Cipher, PrivateKey privateKey) throws Exception {
        byte[] cipherBytes = Base64.getDecoder().decode(base64Cipher);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepSpec = new OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT
        );
        cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepSpec);
        byte[] plain = cipher.doFinal(cipherBytes);
        return new String(plain);
    }

    /**
     * 私钥签名 SHA256withRSA
     */
    public static String sign(String content, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(content.getBytes());
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }

    /**
     * 公钥验签
     */
    public static boolean verify(String content, String signBase64, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(content.getBytes());
        byte[] signBytes = Base64.getDecoder().decode(signBase64);
        return signature.verify(signBytes);
    }

    // base64字符串转公钥
    public static PublicKey getPublicKey(String pubKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(pubKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // base64字符串转私钥
    public static PrivateKey getPrivateKey(String priKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(priKeyBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }


    public static void main(String[] args) throws Exception {
        // 1.生成密钥对 RSA‑2048
        KeyPair keyPair = generateRsaKeyPair(2048);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 输出 base64 密钥（可保存，注意：这里是原始key的base64，不是PEM带-----BEGIN的格式）
        String pubBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String priBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("公钥base64:\n" + pubBase64);
        System.out.println("私钥base64:\n" + priBase64);

        // 2.加密解密
        String msg = "hello rsa 测试123";
        String cipher = encryptByPublicKey(msg, publicKey);
        System.out.println("RSA密文:" + cipher);
        String plain = decryptByPrivateKey(cipher, privateKey);
        System.out.println("解密结果:" + plain);

        // 3.签名验签
        String sign = sign(msg, privateKey);
        System.out.println("签名:" + sign);
        boolean ok = verify(msg, sign, publicKey);
        System.out.println("验签结果:" + ok);
    }
}
