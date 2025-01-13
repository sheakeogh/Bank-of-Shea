package com.bank.backend.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.backend.exception.ResourceNotFoundException;
import com.bank.backend.model.Account;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createNewAccount(Account newAccount) {
        return accountRepository.save(newAccount);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(("Cannot Find Account With ID: " + id)));
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        return accountRepository.findById(id)
        .map(existingAccount -> updateExistingAccount(existingAccount, account))
        .orElseThrow(() -> new ResourceNotFoundException(("Cannot Find Account With ID: " + id)));  
    }

    private Account updateExistingAccount(Account existingAccount, Account account) {
        existingAccount.setName(account.getName());
        existingAccount.setEmail(account.getEmail());
        existingAccount.setPhone(account.getPhone());
        existingAccount.setAddress(account.getAddress());
        
        return accountRepository.save(existingAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException(("Cannot Find Account With ID: " + id));
        }

        accountRepository.deleteById(id);
    }

}
