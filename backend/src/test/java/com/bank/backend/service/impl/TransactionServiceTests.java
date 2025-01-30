package com.bank.backend.service.impl;

import com.bank.backend.model.*;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.TransactionRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewTransactionSuccess() {
        User user = createUser();
        Transaction transaction = createTransaction();
        Account account = createAccount();

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        TransactionDTO transactionDTO = transactionService.createNewTransaction(transaction, "accessToken");

        Assertions.assertNotNull(transactionDTO);

        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    public void testCreateNewTransactionNullUser() {
        Transaction transaction = createTransaction();
        User user = createUser();

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        TransactionDTO transactionDTO = transactionService.createNewTransaction(transaction, "accessToken");

        Assertions.assertNull(transactionDTO);

        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testCreateNewTransactionNoAccount() {
        User user = createUser();
        Transaction transaction = createTransaction();
        user.getAccounts().get(0).setAccountType(AccountType.SAVINGS);

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        TransactionDTO transactionDTO = transactionService.createNewTransaction(transaction, "accessToken");

        Assertions.assertNull(transactionDTO);

        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Name");
        user.setPassword("Password");
        user.setUserRole(UserRole.USER);
        user.setAccounts(List.of(createAccount()));
        user.setTokens(Collections.emptyList());

        return user;
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

    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(20.00);
        transaction.setDescription("Payment");
        transaction.setId(1L);
        transaction.setAccountType(AccountType.CURRENT);
        transaction.setAccount(createAccount());

        return transaction;
    }

}
