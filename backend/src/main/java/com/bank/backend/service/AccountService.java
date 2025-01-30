package com.bank.backend.service;

import java.util.List;
import com.bank.backend.model.Account;
import com.bank.backend.model.AccountDTO;

public interface AccountService {
    
    AccountDTO createNewAccount(Account newAccount, String token);
    List<AccountDTO> getAllAccounts();
    AccountDTO getAccountById(Long id);
    AccountDTO updateAccount(Long id, Account account);
    void deleteAccount(Long id);

}
