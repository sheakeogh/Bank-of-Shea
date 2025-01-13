package com.bank.backend.service.impl;

import com.bank.backend.exception.ResourceNotFoundException;
import com.bank.backend.model.Account;
import com.bank.backend.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AccountServiceImplTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountSuccess() {
        Account account = createAccount();

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        Account actualAccount = accountService.createNewAccount(account);

        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(account, actualAccount);

        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testGetAllAccountsSuccess() {
        Account account1 = createAccount();
        Account account2 = createAccount();

        List<Account> expectedAccountList = Arrays.asList(account1, account2);

        Mockito.when(accountRepository.findAll()).thenReturn(expectedAccountList);

        List<Account> actualAccounts = accountService.getAllAccounts();

        Assertions.assertNotNull(actualAccounts);
        Assertions.assertEquals(2, actualAccounts.size());
        Assertions.assertTrue(actualAccounts.contains(account1));
        Assertions.assertTrue(actualAccounts.contains(account2));

        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetAccountByIdSuccess() {
        Account account = createAccount();

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));

        Account actualAccount = accountService.getAccountById(1L);

        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(account, actualAccount);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void testGetAccountByIdException() {
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(1L);
        });

        Assertions.assertEquals("Cannot Find Account With ID: 1", exception.getMessage());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void testUpdateAccountSuccess() {
        Account account = createAccount();

        Account updatedAccount = new Account();
        updatedAccount.setId(1L);
        updatedAccount.setName("NewName");
        updatedAccount.setPhone("654321");
        updatedAccount.setEmail("newmail@newmail.com");
        updatedAccount.setAddress("NewAddress");

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updatedAccount);

        Account actualAccount = accountService.updateAccount(1L, updatedAccount);

        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(updatedAccount, actualAccount);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testUpdateAccountException() {
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            accountService.updateAccount(1L, Mockito.any(Account.class));
        });

        Assertions.assertEquals("Cannot Find Account With ID: 1", exception.getMessage());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void testDeleteAccountSuccess() {
        Mockito.when(accountRepository.existsById(Mockito.any())).thenReturn(true);
        Mockito.doNothing().when(accountRepository).deleteById(Mockito.any());

        accountService.deleteAccount(1L);

        Mockito.verify(accountRepository, Mockito.times(1)).existsById(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    public void testDeleteAccountException() {
        Mockito.when(accountRepository.existsById(Mockito.any())).thenReturn(false);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deleteAccount(1L);
        });

        Assertions.assertEquals("Cannot Find Account With ID: 1", exception.getMessage());
        Mockito.verify(accountRepository, Mockito.times(1)).existsById(Mockito.any());
    }


    private Account createAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Name");
        account.setPhone("123456");
        account.setEmail("mail@mail.com");
        account.setAddress("Address");

        return account;
    }
}
