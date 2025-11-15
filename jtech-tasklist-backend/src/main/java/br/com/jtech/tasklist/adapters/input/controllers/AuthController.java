/*
*  @(#)AuthController.java
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
package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.AuthResponse;
import br.com.jtech.tasklist.adapters.input.protocols.LoginRequest;
import br.com.jtech.tasklist.adapters.input.protocols.RegisterRequest;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.AuthInputGateway;
import br.com.jtech.tasklist.config.infra.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* class AuthController
* 
* @author jtech
*/
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthInputGateway authInputGateway;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(request.getPassword())
            .build();
        
        User savedUser = authInputGateway.register(user);
        
        String token = jwtTokenProvider.generateToken(savedUser.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getId());
        
        AuthResponse response = AuthResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .type("Bearer")
            .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authInputGateway.authenticate(request.getEmail(), request.getPassword());
        
        String token = jwtTokenProvider.generateToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        
        AuthResponse response = AuthResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .type("Bearer")
            .build();
        
        return ResponseEntity.ok(response);
    }
}


