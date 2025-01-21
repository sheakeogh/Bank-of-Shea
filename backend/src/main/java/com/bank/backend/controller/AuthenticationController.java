package com.bank.backend.controller;

import com.bank.backend.model.User;
import com.bank.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }

    @GetMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshToken(request, response));
    }
}