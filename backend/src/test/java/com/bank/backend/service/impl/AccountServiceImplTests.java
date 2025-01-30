package com.bank.backend.service.impl;

import com.bank.backend.exception.ResourceNotFoundException;
import com.bank.backend.model.*;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AccountServiceImplTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountSuccess() {
        Account account = createAccount();
        AccountDTO accountDTO = createAccountDTO();
        User user = createUser();

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        AccountDTO actualAccount = accountService.createNewAccount(account, "accessToken");

        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(accountDTO.getFirstName(), actualAccount.getFirstName());
        Assertions.assertEquals(accountDTO.getLastName(), actualAccount.getLastName());
        Assertions.assertEquals(accountDTO.getBalance(), actualAccount.getBalance());
        Assertions.assertEquals(accountDTO.getAccountNumber(), actualAccount.getAccountNumber());
        Assertions.assertEquals(accountDTO.getAccountType(), actualAccount.getAccountType());
        Assertions.assertEquals(accountDTO.getUserId(), actualAccount.getUserId());

        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testCreateAccountFail() {
        Account account = createAccount();
        User user = createUser();

        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        AccountDTO actualAccount = accountService.createNewAccount(account, "accessToken");

        Assertions.assertNull(actualAccount);

        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testGetAllAccountsSuccess() {
        List<Account> accountList = List.of(createAccount());
        List<AccountDTO> accountDTOList = List.of(createAccountDTO());

        Mockito.when(accountRepository.findAll()).thenReturn(accountList);

        List<AccountDTO> actualAccounts = accountService.getAllAccounts();

        Assertions.assertNotNull(actualAccounts);
        Assertions.assertEquals(1, actualAccounts.size());
        Assertions.assertEquals(accountDTOList.get(0).getFirstName(), actualAccounts.get(0).getFirstName());
        Assertions.assertEquals(accountDTOList.get(0).getLastName(), actualAccounts.get(0).getLastName());
        Assertions.assertEquals(accountDTOList.get(0).getBalance(), actualAccounts.get(0).getBalance());
        Assertions.assertEquals(accountDTOList.get(0).getAccountNumber(), actualAccounts.get(0).getAccountNumber());
        Assertions.assertEquals(accountDTOList.get(0).getAccountType(), actualAccounts.get(0).getAccountType());
        Assertions.assertEquals(accountDTOList.get(0).getUserId(), actualAccounts.get(0).getUserId());

        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetAccountByIdSuccess() {
        Account account = createAccount();
        AccountDTO accountDTO = createAccountDTO();

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));

        AccountDTO actualAccount = accountService.getAccountById(1L);

        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(accountDTO.getFirstName(), actualAccount.getFirstName());
        Assertions.assertEquals(accountDTO.getLastName(), actualAccount.getLastName());
        Assertions.assertEquals(accountDTO.getBalance(), actualAccount.getBalance());
        Assertions.assertEquals(accountDTO.getAccountNumber(), actualAccount.getAccountNumber());
        Assertions.assertEquals(accountDTO.getAccountType(), actualAccount.getAccountType());
        Assertions.assertEquals(accountDTO.getUserId(), actualAccount.getUserId());

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
        updatedAccount.setFirstName("NewName");
        updatedAccount.setLastName("NewName");
        updatedAccount.setAccountNumber("654321");
        updatedAccount.setBalance(5.00);
        updatedAccount.setAccountType(AccountType.SAVINGS);
        updatedAccount.setUserAccount(createUser());

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updatedAccount);

        AccountDTO actualAccount = accountService.updateAccount(1L, updatedAccount);

        Assertions.assertNotNull(actualAccount);

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

    private AccountDTO createAccountDTO() {
        AccountDTO account = new AccountDTO();
        account.setFirstName("Name");
        account.setLastName("Name");
        account.setAccountNumber("123456");
        account.setBalance(50.00);
        account.setAccountType(AccountType.CURRENT);
        account.setUserId(1);

        return account;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setFirstName("Name");
        account.setLastName("Name");
        account.setAccountNumber("123456");
        account.setBalance(50.00);
        account.setAccountType(AccountType.CURRENT);
        account.setUserAccount(createUser());

        return account;
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Name");
        user.setPassword("Password");
        user.setUserRole(UserRole.USER);
        user.setAccounts(Collections.emptyList());
        user.setTokens(Collections.emptyList());

        return user;
    }

}
