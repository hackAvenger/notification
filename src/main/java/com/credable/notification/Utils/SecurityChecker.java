package com.credable.notification.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger( CryptoUtils.class);

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String  encryptData(String key,String data)  {
        try {
            byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);
            byte[] ivspec = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);

            SecretKey secretKey = CryptoUtils.generateKey(key,salt);
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            // ASE-GCM needs GCMParameterSpec
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, ivspec));

            byte[] cipherText = cipher.doFinal(data.getBytes());

            // prefix IV and Salt to cipher text
            byte[] cipherTextWithIvSalt = ByteBuffer.allocate(ivspec.length + salt.length + cipherText.length).put(ivspec).put(salt).
                    put(cipherText).array();

            // string representation, base64, send this string to other for decryption.
            return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);

        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();

        }
        return null;
    }


    // we need the same password, salt and iv to decrypt it
    public static String decrypt(String key,String data) {

        try {
            byte[] decode = Base64.getDecoder().decode(data.getBytes(UTF_8));
            ByteBuffer bb = ByteBuffer.wrap(decode);

            byte[] ivspec = new byte[IV_LENGTH_BYTE];
            bb.get(ivspec);

            byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);
            bb.get(salt);

            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);

            SecretKey secretKey = CryptoUtils.generateKey(key,salt);

            // get back the aes key from the same password and salt
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, ivspec));

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
        return null;

    }
}
