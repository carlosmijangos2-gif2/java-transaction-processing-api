package com.example.demo.transactionstorage.service;

import java.util.UUID;

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

        transaction.setReferencia(UUID.randomUUID().toString());

        transaction.setEstatus(request.getEstatus());

        return repository.save(transaction);
    }
}