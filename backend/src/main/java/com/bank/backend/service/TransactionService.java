package com.bank.backend.service;

import com.bank.backend.model.Transaction;
import com.bank.backend.model.TransactionDTO;

public interface TransactionService {

    TransactionDTO createNewTransaction(Transaction newTransaction, String token);

}
