package com.bank.backend.service.impl;

import com.bank.backend.model.*;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.backend.repository.TransactionRepository;
import com.bank.backend.service.TransactionService;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TransactionDTO createNewTransaction(Transaction newTransaction, String token) {
        Account account = getAccount(newTransaction.getAccountType(), token);
        if (account == null) {
            return null;
        }

        Transaction transaction = saveTransaction(newTransaction, account);

        return mapToDto(transaction);
    }

    private Transaction saveTransaction(Transaction transaction, Account account) {
        transaction.setAccount(account);
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);

        return transactionRepository.save(transaction);
    }

    private TransactionDTO mapToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setAccountId(transaction.getAccount().getId());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAccountType(transaction.getAccountType());

        return transactionDTO;
    }

    private Account getAccount(AccountType accountType, String token) {
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return null;
        }

        for (Account account : user.get().getAccounts()) {
            if (account.getAccountType() == accountType) {
                return account;
            }
        }

        return null;
    }

}
