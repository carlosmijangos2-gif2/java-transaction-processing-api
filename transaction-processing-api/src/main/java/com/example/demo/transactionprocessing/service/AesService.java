package com.example.demo.transactionprocessing.service;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AesService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_256_KEY_LENGTH = 32;
    // Tamaño recomendado del IV para GCM: 12 bytes
    private static final int IV_LENGTH = 12;

    // Authentication Tag: 128 bits
    private static final int TAG_LENGTH = 128;

    private final SecretKeySpec secretKey;

    public AesService(@Value("${security.aes.key}") String key) {

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

      
        

        if (keyBytes.length != AES_256_KEY_LENGTH) {
            throw new IllegalArgumentException(
                "La clave AES-256 debe tener exactamente 32 bytes"
            );
        }

        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plainText) {

        try {

            // 1. Generar IV aleatorio
            byte[] iv = new byte[IV_LENGTH];

            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);


            // 2. Crear Cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);


            // 3. Configurar GCM
            GCMParameterSpec gcmSpec =
                    new GCMParameterSpec(TAG_LENGTH, iv);


            // 4. Inicializar para cifrar
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    gcmSpec
            );


            // 5. Cifrar
            byte[] encrypted =
                    cipher.doFinal(
                            plainText.getBytes(StandardCharsets.UTF_8)
                    );


            // 6. Unir IV + texto cifrado
            ByteBuffer buffer =
                    ByteBuffer.allocate(iv.length + encrypted.length);

            buffer.put(iv);
            buffer.put(encrypted);


            // 7. Convertir a Base64
            return Base64.getEncoder()
                    .encodeToString(buffer.array());

        } catch (Exception e) {

            throw new IllegalStateException(e.getMessage(),
                    e.getCause()
            );
        }
    }
    public String decrypt(String encryptedText) {

        try {

            // 1. Decodificar Base64
            byte[] decoded =
                    Base64.getDecoder().decode(encryptedText);

            // Validar tamaño mínimo
            if (decoded.length <= IV_LENGTH) {
                throw new IllegalArgumentException(
                        "Texto cifrado inválido"
                );
            }

            // 2. Extraer IV
            byte[] iv = Arrays.copyOfRange(
                    decoded,
                    0,
                    IV_LENGTH
            );

            // 3. Extraer contenido cifrado + authentication tag
            byte[] encrypted = Arrays.copyOfRange(
                    decoded,
                    IV_LENGTH,
                    decoded.length
            );

            // 4. Crear Cipher
            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);

            // 5. Configurar GCM
            GCMParameterSpec gcmSpec =
                    new GCMParameterSpec(
                            TAG_LENGTH,
                            iv
                    );

            // 6. Inicializar para descifrar
            
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    gcmSpec
            );

            // 7. Descifrar
            byte[] decrypted =
                    cipher.doFinal(encrypted);

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(e.toString(),
                    e
            );
        }
    }
    private String fingerprint(byte[] keyBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(keyBytes);

            return HexFormat.of().formatHex(hash).substring(0, 12);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}