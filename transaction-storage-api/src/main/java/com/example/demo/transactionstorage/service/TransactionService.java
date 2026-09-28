package com.example.demo.transactionstorage.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.example.demo.transactionstorage.dto.TransactionRequest;
import com.example.demo.transactionstorage.entity.TransactionEntity;
import com.example.demo.transactionstorage.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionEntity save(TransactionRequest request) {

        TransactionEntity transaction = new TransactionEntity();

        transaction.setOperacion(request.getOperacion());
        transaction.setImporte(request.getImporte());
        transaction.setCliente(request.getCliente());

        int referencia = ThreadLocalRandom.current()
                .nextInt(100000, 1000000);

        transaction.setReferencia(String.valueOf(referencia));

        transaction.setEstatus("APROBADA");

        return repository.save(transaction);
    }
}