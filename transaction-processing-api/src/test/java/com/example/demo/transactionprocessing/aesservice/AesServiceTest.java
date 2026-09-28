package com.example.demo.transactionprocessing.aesservice;

import org.junit.jupiter.api.Test;

import com.example.demo.transactionprocessing.service.AesService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;


class AesServiceTest {
	@Test
	void debeCifrarYDescifrarCorrectamente() throws Exception {

        // Arrange
		 String key = System.getenv("AES_SECRET_KEY");
		
		    String textoOriginal = "prueba";

		    assertNotNull(key, "La variable AES_SECRET_KEY debe estar configurada");

		    AesService aesService = new AesService(key);

        // Act
        String textoCifrado = aesService.encrypt(textoOriginal);
        String textoDescifrado = aesService.decrypt(textoCifrado);

        
        // Assert
assertEquals(textoOriginal, textoDescifrado);
    }
}