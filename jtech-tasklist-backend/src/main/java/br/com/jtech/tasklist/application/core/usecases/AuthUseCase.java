/*
*  @(#)AuthUseCase.java
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
import br.com.jtech.tasklist.application.ports.input.AuthInputGateway;
import br.com.jtech.tasklist.application.ports.output.AuthOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* class AuthUseCase  
* 
* @author jtech
*/
public class AuthUseCase implements AuthInputGateway {

    private final AuthOutputGateway authOutputGateway;
    private final PasswordEncoder passwordEncoder;

    public AuthUseCase(AuthOutputGateway authOutputGateway, PasswordEncoder passwordEncoder) {
        this.authOutputGateway = authOutputGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        if (authOutputGateway.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        return authOutputGateway.save(user);
    }

    @Override
    public User authenticate(String email, String password) {
        User user = authOutputGateway.findByEmail(email)
            .orElseThrow(() -> new BusinessException("Invalid email or password"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }
        
        return user;
    }
}


