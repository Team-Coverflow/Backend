package com.coverflow.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final SecureRandom secureRandom = new SecureRandom(); // 안전한 무작위 생성기

    private static String PRIVATE_KEY; // 암호화에 사용할 키 (16, 24, 32 bytes)

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }

    @Value("${aes.private-key}")
    private void setPrivateKey(String key) {
        AesUtil.PRIVATE_KEY = key;
    }

    public static String encrypt(final String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(getPrivateKey().getBytes(StandardCharsets.UTF_8), "AES");

        // IV 생성
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // IV를 암호문 앞에 붙여서 반환
        byte[] encryptedIVAndText = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedIVAndText, iv.length, encryptedBytes.length);

        return Base64.getUrlEncoder().encodeToString(encryptedIVAndText);
    }

    public static String decrypt(final String encryptedIvText) throws Exception {
        byte[] decoded = Base64.getUrlDecoder().decode(encryptedIvText);

        // IV와 암호문 분리
        byte[] iv = new byte[16];
        byte[] encryptedText = new byte[decoded.length - iv.length];
        System.arraycopy(decoded, 0, iv, 0, iv.length);
        System.arraycopy(decoded, iv.length, encryptedText, 0, encryptedText.length);

        SecretKeySpec secretKey = new SecretKeySpec(getPrivateKey().getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedText);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
