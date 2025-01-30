package com.bank.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.bank.backend.model.Transaction;
import com.bank.backend.service.TransactionService;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction newTransaction, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createNewTransaction(newTransaction, token));
    }
}
