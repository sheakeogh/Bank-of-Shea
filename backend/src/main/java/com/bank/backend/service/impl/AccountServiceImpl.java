package com.bank.backend.service.impl;

import java.util.List;
import java.util.Optional;
import com.bank.backend.model.AccountDTO;
import com.bank.backend.model.User;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.JwtService;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public AccountDTO createNewAccount(Account newAccount, String token) {
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return null;
        }

        Account account = saveAccount(newAccount, user.get());

        return mapToDto(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        List<AccountDTO> accountDTOList = accountList.stream()
                .map(this::mapToDto)
                .toList();

        return accountDTOList;
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Cannot Find Account With ID: " + id)));

        return mapToDto(account);
    }

    @Override
    public AccountDTO updateAccount(Long id, Account account) {
        Account updatedAccount = accountRepository.findById(id)
                .map(existingAccount -> updateExistingAccount(existingAccount, account))
                .orElseThrow(() -> new ResourceNotFoundException(("Cannot Find Account With ID: " + id)));

        return mapToDto(updatedAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException(("Cannot Find Account With ID: " + id));
        }

        accountRepository.deleteById(id);
    }

    private Account updateExistingAccount(Account existingAccount, Account account) {
        existingAccount.setFirstName(account.getFirstName());
        existingAccount.setLastName(account.getLastName());
        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setAccountType(account.getAccountType());

        return accountRepository.save(existingAccount);
    }

    private Account saveAccount(Account account, User user) {
        account.setUserAccount(user);

        return accountRepository.save(account);
    }

    private AccountDTO mapToDto(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setFirstName(account.getFirstName());
        accountDTO.setLastName(account.getLastName());
        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setUserId(account.getUserAccount().getId());

        return accountDTO;
    }

}
