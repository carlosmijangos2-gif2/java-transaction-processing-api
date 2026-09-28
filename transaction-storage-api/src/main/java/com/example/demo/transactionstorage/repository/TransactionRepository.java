package com.example.demo.transactionstorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.transactionstorage.entity.TransactionEntity;

public interface TransactionRepository
        extends JpaRepository<TransactionEntity, Long> {

}