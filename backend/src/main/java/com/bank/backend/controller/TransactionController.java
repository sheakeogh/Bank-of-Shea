package com.bank.backend.controller;

import com.bank.backend.model.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.backend.model.Transaction;
import com.bank.backend.service.TransactionService;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    @Operation(summary = "Create Transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New Transaction Made", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> createTransaction(@RequestBody Transaction newTransaction, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createNewTransaction(newTransaction, token));
    }
}
