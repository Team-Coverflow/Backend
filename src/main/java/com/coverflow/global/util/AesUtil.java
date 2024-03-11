package com.coverflow.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AesUtil {
    private static final String ALGORITHM = "AES";

    private static String PRIVATE_KEY; // 암호화에 사용할 키 (16, 24, 32 bytes)

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }

    @Value("${aes.private-key}")
    private void setPrivateKey(String key) {
        AesUtil.PRIVATE_KEY = key;
    }

    public static String encrypt(final String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(getPrivateKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // URL 안전한 Base64 인코딩 사용
        return Base64.getUrlEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(final String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(getPrivateKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // URL 안전한 Base64 디코딩 사용
        byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
