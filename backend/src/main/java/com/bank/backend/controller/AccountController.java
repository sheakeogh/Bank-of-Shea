package com.bank.backend.controller;

import java.util.List;

import com.bank.backend.model.Account;
import com.bank.backend.model.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.backend.service.AccountService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    @Operation(summary = "Create New Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New Account Created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> createAccount(@RequestBody Account newAccount, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        AccountDTO account = accountService.createNewAccount(newAccount, token);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Creating Account. Please Try Again.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/accounts")
    @Operation(summary = "Get All Accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return All Accounts", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getAllAccounts(@RequestHeader("Authorization") String tokenHeader) {
        System.out.println(tokenHeader);
        List<AccountDTO> accountList = accountService.getAllAccounts();

        if (accountList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Accounts Found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }

    @GetMapping("/account/{id}")
    @Operation(summary = "Get Accounts by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Account by ID", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccountById(id));
    }

    @PutMapping("/account/{id}")
    @Operation(summary = "Update Account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Updated Account", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/account/{id}")
    @Operation(summary = "Delete Account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted Account", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);

        return ResponseEntity.status(HttpStatus.OK).body("Account Deleted Successfully.");
    }

}
