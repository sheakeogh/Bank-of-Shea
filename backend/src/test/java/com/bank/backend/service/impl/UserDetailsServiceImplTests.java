package com.bank.backend.service.impl;

import com.bank.backend.model.User;
import com.bank.backend.model.UserRole;
import com.bank.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

public class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        User user = createUser();

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetails actualUserDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Assertions.assertNotNull(actualUserDetails);
        Assertions.assertEquals(user.getUsername(), actualUserDetails.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
    }

    @Test
    public void testLoadUserByUsernameFail() {
        User user = createUser();

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(user.getUsername());
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("User Not Found.", exception.getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
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

}
