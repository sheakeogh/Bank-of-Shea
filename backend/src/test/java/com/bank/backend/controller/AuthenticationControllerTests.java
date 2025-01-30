package com.bank.backend.controller;

import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.User;
import com.bank.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthenticationControllerTests {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerTestSuccess() {
        AuthenticationResponse authenticationResponse = createAuthenticationResponse("User Registration Was Successful");

        Mockito.when(authenticationService.register(Mockito.any(User.class))).thenReturn(authenticationResponse);

        ResponseEntity<?> response = authenticationController.register(new User());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(authenticationResponse, response.getBody());
        Mockito.verify(authenticationService, Mockito.times(1)).register(Mockito.any(User.class));
    }

    @Test
    public void loginTestSuccess() {
        AuthenticationResponse authenticationResponse = createAuthenticationResponse("User Login Was Successful");

        Mockito.when(authenticationService.authenticate(Mockito.any(User.class))).thenReturn(authenticationResponse);

        ResponseEntity<?> response = authenticationController.login(new User());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(authenticationResponse, response.getBody());
        Mockito.verify(authenticationService, Mockito.times(1)).authenticate(Mockito.any(User.class));
    }

    @Test
    public void refreshTokenTestSuccess() {
        AuthenticationResponse authenticationResponse = createAuthenticationResponse("New Token Generated.");
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(authenticationService.refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn(authenticationResponse);

        ResponseEntity<?> response = authenticationController.refreshToken(mockRequest, mockResponse);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(authenticationResponse, response.getBody());
        Mockito.verify(authenticationService, Mockito.times(1)).refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    private AuthenticationResponse createAuthenticationResponse(String message) {
        return new AuthenticationResponse("accessToken", "refreshToken", message);
    }

}
