package com.bank.backend.service;

import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    AuthenticationResponse register(User request);
    AuthenticationResponse authenticate(User request);
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

}