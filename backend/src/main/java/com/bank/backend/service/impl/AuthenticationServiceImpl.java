package com.bank.backend.service.impl;

import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.Token;
import com.bank.backend.model.User;
import com.bank.backend.repository.TokenRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.AuthenticationService;
import com.bank.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, null,"User Already Exists.");
        }

        User user = saveUser(request);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User Registration Was Successful.");
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("No User Found."));
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User Login Was Successful.");

    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new AuthenticationResponse(null, null, "No Token Found.");
        }

        String token = authHeader.substring(7);

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("No User Found."));

        if(jwtService.isValidRefreshToken(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponse(accessToken, refreshToken, "New Token Generated.");
        }

        return new AuthenticationResponse(null, null, "No Token Found.");

    }

    private User saveUser(User request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(request.getUserRole());

        return userRepository.save(user);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> t.setLoggedOut(true));

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUserToken(user);

        tokenRepository.save(token);
    }

}