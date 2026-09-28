package com.example.demo.transactionprocessing.controller;

import com.example.demo.transactionprocessing.dto.TransactionRequest;
import com.example.demo.transactionprocessing.dto.TransactionResponse;
import com.example.demo.transactionprocessing.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> processTransaction(
          @Valid  @RequestBody TransactionRequest request) {

        TransactionResponse response =
                transactionService.processTransaction(request);

        return ResponseEntity.ok(response);
    }
}