package com.bank.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bank.backend.model.Account;
import com.bank.backend.service.AccountService;

@RestController
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody Account newAccount) {
        Account account = accountService.createNewAccount(newAccount);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Creating Account. Please Try Again.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts() {
        List<Account> accountList = accountService.getAllAccounts();

        if (accountList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Accounts Found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(id));
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);

        return ResponseEntity.status(HttpStatus.OK).body("Account Deleted Successfully.");
    }

}
