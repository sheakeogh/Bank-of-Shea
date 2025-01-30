package com.bank.backend.controller;

import com.bank.backend.model.Account;
import com.bank.backend.model.AccountDTO;
import com.bank.backend.model.AccountType;
import com.bank.backend.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AccountControllerTests {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountSuccess() {
        AccountDTO accountDTO = createAccountDTO();
        Account account = createAccount();

        Mockito.when(accountService.createNewAccount(Mockito.any(Account.class), Mockito.anyString())).thenReturn(accountDTO);

        ResponseEntity<?> response = accountController.createAccount(account, "Bearer accessToken");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(accountDTO, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).createNewAccount(Mockito.any(Account.class), Mockito.anyString());
    }

    @Test
    public void testCreateAccountFail() {
        Account account = createAccount();

        Mockito.when(accountService.createNewAccount(Mockito.any(Account.class), Mockito.anyString())).thenReturn(null);

        ResponseEntity<?> response = accountController.createAccount(account, "Bearer accessToken");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Error Creating Account. Please Try Again.", response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).createNewAccount(Mockito.any(Account.class), Mockito.anyString());
    }

    @Test
    public void testGetAllAccountsSuccess() {
        List<AccountDTO> accountDTOList = Arrays.asList(createAccountDTO(), createAccountDTO());

        Mockito.when(accountService.getAllAccounts()).thenReturn(accountDTOList);

        ResponseEntity<?> response = accountController.getAllAccounts();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(accountDTOList, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).getAllAccounts();
    }

    @Test
    public void testGetAllAccountsFail() {
        Mockito.when(accountService.getAllAccounts()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = accountController.getAllAccounts();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("No Accounts Found.", response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).getAllAccounts();
    }

    @Test
    public void testGetAccountByIdSuccess() {
        AccountDTO accountDTO = createAccountDTO();

        Mockito.when(accountService.getAccountById(Mockito.any())).thenReturn(accountDTO);

        ResponseEntity<?> response = accountController.getAccountById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(accountDTO, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).getAccountById(Mockito.any());
    }

    @Test
    public void testUpdateAccountSuccess() {
        Account account = createAccount();
        AccountDTO accountDTO = createAccountDTO();

        Mockito.when(accountService.updateAccount(Mockito.any(), Mockito.any(Account.class))).thenReturn(accountDTO);

        ResponseEntity<?> response = accountController.updateAccount(1L, account);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(accountDTO, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).updateAccount(Mockito.any(), Mockito.any(Account.class));
    }

    @Test
    public void testDeleteAccountSuccess() {
        Mockito.doNothing().when(accountService).deleteAccount(Mockito.any());

        ResponseEntity<?> response = accountController.deleteAccount(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Account Deleted Successfully.", response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).deleteAccount(Mockito.any());
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

    private AccountDTO createAccountDTO() {
        AccountDTO account = new AccountDTO();
        account.setFirstName("Name");
        account.setLastName("Name");
        account.setAccountNumber("123456");
        account.setBalance(50.00);
        account.setAccountType(AccountType.CURRENT);

        return account;
    }

}