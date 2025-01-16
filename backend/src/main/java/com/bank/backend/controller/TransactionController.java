package com.bank.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bank.backend.model.Transaction;
import com.bank.backend.service.TransactionService;

@RestController
public class TransactionController {
    
    private TransactionService transactionService;

    @PostMapping("/tranasction")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction newTransaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createNewTransaction(newTransaction));
    }
}
