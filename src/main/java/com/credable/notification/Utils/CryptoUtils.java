package com.credable.notification.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger( CryptoUtils.class);

    public static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    // Password derived AES 256 bits secret key
    public static SecretKey generateKey(String keyword,byte[] salt){
        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(keyword.toCharArray(), salt, 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secret;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
        return null;

    }

    public static String encodeToBase64(byte[] byteArray) {
      return Base64.getEncoder().encodeToString(byteArray);
    }
    
    public static byte[] decodeFromBase64(String str) {
      return Base64.getDecoder().decode(str);
    }
    
}
