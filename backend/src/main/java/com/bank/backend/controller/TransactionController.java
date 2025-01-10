package com.bank.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bank.backend.model.Transaction;
import com.bank.backend.service.TransactionService;

@RestController
public class TransactionController {
    
    private TransactionService transactionService;

    @PostMapping("/tranasction")
    public Transaction createTransaction(@RequestBody Transaction newTransaction) {
        return transactionService.createNewTransaction(newTransaction);
    }
}
