package com.bank.backend.configuration;

import com.bank.backend.model.Token;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRole;
import com.bank.backend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import java.util.Collections;
import java.util.Optional;

public class CustomLogoutHandlerTests {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void logoutSuccess() {
        Token token = createToken();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(tokenRepository.findByAccessToken(Mockito.anyString())).thenReturn(Optional.of(token));
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);

        customLogoutHandler.logout(request, response, authentication);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(tokenRepository, Mockito.times(1)).findByAccessToken(Mockito.anyString());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void logoutNullHeader() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        customLogoutHandler.logout(request, response, authentication);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
    }

    @Test
    public void logoutWrongHeader() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Token");

        customLogoutHandler.logout(request, response, authentication);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
    }

    @Test
    public void logoutNullToken() {
        Token token = createToken();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token.getRefreshToken());
        Mockito.when(tokenRepository.findByAccessToken(Mockito.anyString())).thenReturn(Optional.empty());

        customLogoutHandler.logout(request, response, authentication);

        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Mockito.verify(tokenRepository, Mockito.times(1)).findByAccessToken(Mockito.anyString());
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