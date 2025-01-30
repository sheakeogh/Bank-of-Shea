package com.bank.backend.controller;

import com.bank.backend.model.*;
import com.bank.backend.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TransactionControllerTests {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransactionSuccess() {
        Transaction transaction = createTransaction();
        TransactionDTO transactionDTO = createTransactionDTO();

        Mockito.when(transactionService.createNewTransaction(Mockito.any(Transaction.class), Mockito.anyString())).thenReturn(transactionDTO);

        ResponseEntity<?> response = transactionController.createTransaction(transaction, "Bearer accessToken");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(transactionDTO, response.getBody());

        Mockito.verify(transactionService, Mockito.times(1)).createNewTransaction(Mockito.any(Transaction.class), Mockito.anyString());
    }

    private TransactionDTO createTransactionDTO() {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setAccountId(1L);
        transaction.setTransactionType(TransactionType.LODGEMENT);
        transaction.setDescription("Income");
        transaction.setAmount(20.00);

        return transaction;
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
