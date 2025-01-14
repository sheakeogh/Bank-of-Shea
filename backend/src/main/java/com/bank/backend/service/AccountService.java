package com.bank.backend.service;

import java.util.List;
import com.bank.backend.model.Account;

public interface AccountService {
    
    Account createNewAccount(Account newAccount);
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account updateAccount(Long id, Account account);
    void deleteAccount(Long id);

}
