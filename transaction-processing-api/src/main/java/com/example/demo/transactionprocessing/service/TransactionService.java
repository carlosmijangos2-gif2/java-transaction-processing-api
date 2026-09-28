package com.example.demo.transactionprocessing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.transactionprocessing.dto.TransactionRequest;
import com.example.demo.transactionprocessing.dto.TransactionResponse;
import com.example.demo.transactionprocessing.dto.TransactionStorageRequest;
import com.example.demo.transactionprocessing.dto.TransactionStorageResponse;

@Service
public class TransactionService {

    // Dependencias
    private final AesService aesService;
    private final RestTemplate restTemplate;
    private final String storageUrl;

    // Constructor
    public TransactionService(
            AesService aesService,
            RestTemplate restTemplate,
            @Value("${transaction.storage.url}") String storageUrl) {

        this.aesService = aesService;
        this.restTemplate = restTemplate;
        this.storageUrl = storageUrl;
    }
    public TransactionResponse processTransaction(
            TransactionRequest request) {

        // Descifrar secreto AES-256
        String secretoDescifrado =
                aesService.decrypt(request.getSecreto());

        // Solo para comprobar el funcionamiento se comprueba el decifrado 
    //       System.out.println("Secreto descifrado: " + secretoDescifrado
        TransactionStorageRequest storageRequest =
                new TransactionStorageRequest(
                        request.getOperacion(),
                        request.getImporte(),
                        request.getCliente(),"APROBADA"
                        );
     // Invocar API2
        TransactionStorageResponse  responseApi2 =
        		restTemplate.postForObject(
                        storageUrl,
                        storageRequest,
                        TransactionStorageResponse.class
                );

        System.out.println("ID API2: " + responseApi2.getId());
        System.out.println("Operación API2: " + responseApi2.getOperacion());
        System.out.println("Importe API2: " + responseApi2.getImporte());
        System.out.println("Cliente API2: " + responseApi2.getCliente());
        System.out.println("Estatus API2: " + responseApi2.getEstatus());
        System.out.println("Referencia API2: " + responseApi2.getReferencia());
        

        return new TransactionResponse(
                request.getOperacion(),
                request.getCliente(),
                "RECIBIDA"
        );
    }
}