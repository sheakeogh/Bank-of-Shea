package com.bank.backend.service.impl;

import com.bank.backend.model.Account;
import com.bank.backend.model.AccountType;
import com.bank.backend.model.Transaction;
import com.bank.backend.model.TransactionType;
import com.bank.backend.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransactionSuccess() {
        Transaction transaction = createTransaction();

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        Transaction actualTransaction = transactionService.createNewTransaction(transaction);

        Assertions.assertNotNull(actualTransaction);
        Assertions.assertEquals(transaction, actualTransaction);

        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(createAccount());
        transaction.setTransactionType(TransactionType.LODGEMENT);
        transaction.setDescription("Income");
        transaction.setAmount(20.00);

        return transaction;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setFirstName("Name");
        account.setLastName("Name");
        account.setAccountNumber("123456");
        account.setBalance(50.00);
        account.setAccountType(AccountType.CURRENT);

        return account;
    }

}
