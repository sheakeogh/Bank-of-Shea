package com.bank.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userAccount;

    private String firstName;
    private String lastName;
    private String accountNumber;
    private double balance;

    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;

}