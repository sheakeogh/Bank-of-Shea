package com.bank.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    private String firstName;
    private String lastName;
    private String accountNumber;
    private double balance;
    private AccountType accountType;
    private Integer userId;

}
