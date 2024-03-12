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

    public static String[] encrypt(final String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(getPrivateKey().getBytes(StandardCharsets.UTF_8), "AES");

        // IV 생성
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // 암호문을 Base64로 인코딩
        String encodedEncryptedText = Base64.getUrlEncoder().encodeToString(encryptedBytes);
        // IV를 Base64로 인코딩
        String encodedIV = Base64.getUrlEncoder().encodeToString(iv);

        // 암호화된 데이터와 IV 반환
        return new String[]{encodedEncryptedText, encodedIV};
    }

    public static String decrypt(
            final String encryptedText,
            final String ivString
    ) throws Exception {
        byte[] decodedEncryptedText = Base64.getUrlDecoder().decode(encryptedText);
        byte[] iv = Base64.getUrlDecoder().decode(ivString);

        SecretKeySpec secretKey = new SecretKeySpec(getPrivateKey().getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(decodedEncryptedText);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
