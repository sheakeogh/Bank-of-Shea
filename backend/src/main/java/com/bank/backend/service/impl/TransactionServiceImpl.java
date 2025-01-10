package com.bank.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.backend.model.Transaction;
import com.bank.backend.repository.TransactionRepository;
import com.bank.backend.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createNewTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

}
