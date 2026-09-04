package com.easy.interviewsecurity.utils;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Sm2Sm4HybridUtil {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @SuppressWarnings("unused")
    private static final X9ECParameters SM2_CURVE_PARAM = GMNamedCurves.getByName("sm2p256v1");
    private static final String SM4_TRANS = "SM4/CBC/PKCS5Padding";
    private static final String SEP = "||";

    /**
     * 生成SM2密钥对
     */
    public static KeyPair generateSm2KeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("sm2p256v1");
        generator.initialize(ecGenSpec, new SecureRandom());
        return generator.generateKeyPair();
    }

    /**
     * SM2加密（加密SM4会话密钥短字节）
     */
    public static String sm2Encrypt(PublicKey publicKey, byte[] data) throws Exception {
        ECPublicKeyParameters pub = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey);
        CipherParameters param = new ParametersWithRandom(pub, new SecureRandom());
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        engine.init(true, param);
        byte[] enc = engine.processBlock(data, 0, data.length);
        return Hex.toHexString(enc);
    }

    /**
     * SM2解密
     */
    public static byte[] sm2Decrypt(PrivateKey privateKey, String hexCipher) throws Exception {
        ECPrivateKeyParameters pri = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey);
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        engine.init(false, pri);
        byte[] cipherBytes = Hex.decode(hexCipher);
        return engine.processBlock(cipherBytes, 0, cipherBytes.length);
    }

    public static String sm4Encrypt(byte[] sm4Key16, String plainText) throws Exception {
        if (sm4Key16.length != 16) {
            throw new IllegalArgumentException("SM4 key must be 16 bytes");
        }
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec keySpec = new SecretKeySpec(sm4Key16, "SM4");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(SM4_TRANS, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        String ivHex = Hex.toHexString(iv);
        String dataHex = Hex.toHexString(cipherData);
        return ivHex + ":" + dataHex;
    }

    public static String sm4Decrypt(byte[] sm4Key16, String ivHex, String cipherHex) throws Exception {
        if (sm4Key16.length != 16) {
            throw new IllegalArgumentException("SM4 key must be 16 bytes");
        }
        byte[] iv = Hex.decode(ivHex);
        byte[] cipherData = Hex.decode(cipherHex);

        SecretKeySpec keySpec = new SecretKeySpec(sm4Key16, "SM4");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(SM4_TRANS, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plainBytes = cipher.doFinal(cipherData);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }

    /**
     * SM2+SM4混合加密
     * 返回格式 sm2EncKeyHex||sm4IvHex||sm4CipherHex
     */
    public static String hybridEncrypt(String plainText, PublicKey sm2PublicKey) throws Exception {
        byte[] sm4SessionKey = new byte[16];
        new SecureRandom().nextBytes(sm4SessionKey);

        String sm2EncSm4KeyHex = sm2Encrypt(sm2PublicKey, sm4SessionKey);
        String sm4Result = sm4Encrypt(sm4SessionKey, plainText);
        String[] ivAndData = sm4Result.split(":");
        String sm4IvHex = ivAndData[0];
        String sm4CipherHex = ivAndData[1];

        return sm2EncSm4KeyHex + SEP + sm4IvHex + SEP + sm4CipherHex;
    }

    /**
     * SM2+SM4混合解密
     */
    public static String hybridDecrypt(String hybridCipherStr, PrivateKey sm2PrivateKey) throws Exception {
        String[] parts = hybridCipherStr.split("\\|\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("混合密文格式错误");
        }
        String sm2EncSm4KeyHex = parts[0];
        String sm4IvHex = parts[1];
        String sm4CipherHex = parts[2];

        byte[] sm4SessionKey = sm2Decrypt(sm2PrivateKey, sm2EncSm4KeyHex);
        if (sm4SessionKey.length != 16) {
            throw new RuntimeException("SM2解密得到SM4密钥长度非法");
        }
        return sm4Decrypt(sm4SessionKey, sm4IvHex, sm4CipherHex);
    }

    public static void main(String[] args) throws Exception {
        KeyPair sm2KeyPair = generateSm2KeyPair();
        PublicKey pubKey = sm2KeyPair.getPublic();
        PrivateKey priKey = sm2KeyPair.getPrivate();

        String businessText = "这是国密SM2+SM4混合加密测试，支持中文，大报文也可以。{\"user\":\"test\",\"id\":10001}";
        System.out.println("原始明文：" + businessText);

        String hybridCipher = hybridEncrypt(businessText, pubKey);
        System.out.println("混合密文：" + hybridCipher);

        String decryptText = hybridDecrypt(hybridCipher, priKey);
        System.out.println("解密结果：" + decryptText);
    }
}
