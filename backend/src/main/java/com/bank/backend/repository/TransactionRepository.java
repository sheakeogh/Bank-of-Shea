package com.bank.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.backend.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
