package com.bank.backend.service.impl;

import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.Token;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRole;
import com.bank.backend.repository.TokenRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AuthenticationServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        User user = createUser();
        Token token = createToken();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("accessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("refreshToken");
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);

        AuthenticationResponse authenticationResponse = authenticationService.register(user);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("accessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Registration Was Successful.", authenticationResponse.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testRegisterFail() {
        User user = createUser();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        AuthenticationResponse authenticationResponse = authenticationService.register(user);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertNull(authenticationResponse.getAccessToken());
        Assertions.assertNull(authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Already Exists.", authenticationResponse.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testAuthenticateSuccess() {
        User user = createUser();
        Token token = createToken();

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("accessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("refreshToken");
        Mockito.when(tokenRepository.findAllAccessTokensByUser(Mockito.any(Integer.class))).thenReturn(List.of(token));
        Mockito.when(tokenRepository.saveAll(Mockito.anyList())).thenReturn(List.of(token));
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(user);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("accessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Login Was Successful.", authenticationResponse.getMessage());
        Assertions.assertTrue(user.isAccountNonExpired());
        Assertions.assertTrue(user.isAccountNonLocked());
        Assertions.assertTrue(user.isCredentialsNonExpired());
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertNotNull(user.getAuthorities());

        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).findAllAccessTokensByUser(Mockito.any(Integer.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).saveAll(Mockito.anyList());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testAuthenticateFail() {
        User user = createUser();

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(user);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("No User Found.", exception.getMessage());

        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testAuthenticateNoTokens() {
        User user = createUser();
        Token token = createToken();

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("accessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("refreshToken");
        Mockito.when(tokenRepository.findAllAccessTokensByUser(Mockito.any(Integer.class))).thenReturn(Collections.emptyList());
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(user);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("accessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Login Was Successful.", authenticationResponse.getMessage());

        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).findAllAccessTokensByUser(Mockito.any(Integer.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testRefreshTokenSuccess() {
        User user = createUser();
        Token token = createToken();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class))).thenReturn(true);
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("newAccessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("newRefreshToken");
        Mockito.when(tokenRepository.findAllAccessTokensByUser(Mockito.any(Integer.class))).thenReturn(List.of(token));
        Mockito.when(tokenRepository.saveAll(Mockito.anyList())).thenReturn(List.of(token));
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(mockRequest, mockResponse);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("newAccessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("newRefreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("New Token Generated.", authenticationResponse.getMessage());

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).findAllAccessTokensByUser(Mockito.any(Integer.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).saveAll(Mockito.anyList());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testRefreshTokenNullHeader() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletRequest mockRequestNull = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Test: Token");
        Mockito.when(mockRequestNull.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(mockRequest, mockResponse);
        AuthenticationResponse authenticationResponseNullHeader = authenticationService.refreshToken(mockRequestNull, mockResponse);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertNull(authenticationResponse.getAccessToken());
        Assertions.assertNull(authenticationResponse.getRefreshToken());
        Assertions.assertEquals("No Token Found.", authenticationResponse.getMessage());

        Assertions.assertNotNull(authenticationResponseNullHeader);
        Assertions.assertNull(authenticationResponseNullHeader.getAccessToken());
        Assertions.assertNull(authenticationResponseNullHeader.getRefreshToken());
        Assertions.assertEquals("No Token Found.", authenticationResponseNullHeader.getMessage());

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(Mockito.any());
        Mockito.verify(mockRequestNull, Mockito.times(1)).getHeader(Mockito.any());
    }

    @Test
    public void testRefreshTokenNullUser() {
        User user = createUser();
        Token token = createToken();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            authenticationService.refreshToken(mockRequest, mockResponse);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("No User Found.", exception.getMessage());

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testRefreshTokenInvalidToken() {
        User user = createUser();
        Token token = createToken();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class))).thenReturn(false);

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(mockRequest, mockResponse);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertNull(authenticationResponse.getAccessToken());
        Assertions.assertNull(authenticationResponse.getRefreshToken());
        Assertions.assertEquals("No Token Found.", authenticationResponse.getMessage());

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class));
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Name");
        user.setPassword("Password");
        user.setUserRole(UserRole.USER);
        user.setAccounts(Collections.emptyList());
        user.setTokens(Collections.emptyList());

        return user;
    }

    private Token createToken() {
        Token token = new Token();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setLoggedOut(false);
        token.setUserToken(createUser());

        return token;
    }
}
