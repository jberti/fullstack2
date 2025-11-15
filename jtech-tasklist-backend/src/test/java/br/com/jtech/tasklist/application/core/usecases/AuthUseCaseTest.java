/*
*  @(#)AuthUseCaseTest.java
*
*  Copyright (c) J-Tech Solucoes em Informatica.
*  All Rights Reserved.
*
*  This software is the confidential and proprietary information of J-Tech.
*  ("Confidential Information"). You shall not disclose such Confidential
*  Information and shall use it only in accordance with the terms of the
*  license agreement you entered into with J-Tech.
*
*/
package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.AuthOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
* class AuthUseCaseTest 
* 
* @author jtech
*/
@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private AuthOutputGateway authOutputGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id("123e4567-e89b-12d3-a456-426614174000")
            .name("Test User")
            .email("test@example.com")
            .password("password123")
            .build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Given
        String originalPassword = user.getPassword();
        when(authOutputGateway.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User savedUser = User.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .password("encodedPassword")
            .build();
        when(authOutputGateway.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = authUseCase.register(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        verify(authOutputGateway).existsByEmail(user.getEmail());
        verify(passwordEncoder).encode(originalPassword);
        verify(authOutputGateway).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(authOutputGateway.existsByEmail(anyString())).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> authUseCase.register(user))
            .isInstanceOf(BusinessException.class)
            .hasMessage("Email already registered");
        
        verify(authOutputGateway).existsByEmail(user.getEmail());
        verify(authOutputGateway, never()).save(any(User.class));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        User savedUser = User.builder()
            .id("123e4567-e89b-12d3-a456-426614174000")
            .email(email)
            .password("encodedPassword")
            .build();
        
        when(authOutputGateway.findByEmail(email)).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(password, savedUser.getPassword())).thenReturn(true);

        // When
        User result = authUseCase.authenticate(email, password);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        verify(authOutputGateway).findByEmail(email);
        verify(passwordEncoder).matches(password, savedUser.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        // Given
        String email = "nonexistent@example.com";
        String password = "password123";
        
        when(authOutputGateway.findByEmail(email)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> authUseCase.authenticate(email, password))
            .isInstanceOf(BusinessException.class)
            .hasMessage("Invalid email or password");
        
        verify(authOutputGateway).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Given
        String email = "test@example.com";
        String password = "wrongPassword";
        User savedUser = User.builder()
            .id("123e4567-e89b-12d3-a456-426614174000")
            .email(email)
            .password("encodedPassword")
            .build();
        
        when(authOutputGateway.findByEmail(email)).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(password, savedUser.getPassword())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> authUseCase.authenticate(email, password))
            .isInstanceOf(BusinessException.class)
            .hasMessage("Invalid email or password");
        
        verify(authOutputGateway).findByEmail(email);
        verify(passwordEncoder).matches(password, savedUser.getPassword());
    }
}


