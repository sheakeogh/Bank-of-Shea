package com.bank.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.backend.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
