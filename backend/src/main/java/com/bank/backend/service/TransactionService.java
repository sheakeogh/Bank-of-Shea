package com.bank.backend.service;

import com.bank.backend.model.Transaction;

public interface TransactionService {
    
    Transaction createNewTransaction(Transaction newTransaction);

}
