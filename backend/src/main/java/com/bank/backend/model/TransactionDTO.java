package com.bank.backend.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Long accountId;
    private String description;
    private Double amount;
    private AccountType accountType;

}
