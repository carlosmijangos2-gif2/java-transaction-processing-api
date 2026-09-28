package com.example.demo.transactionstorage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.transactionstorage.dto.TransactionRequest;
import com.example.demo.transactionstorage.entity.TransactionEntity;
import com.example.demo.transactionstorage.service.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionEntity> createTransaction(
            @RequestBody TransactionRequest request) {

        TransactionEntity transaction =
                transactionService.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }
}