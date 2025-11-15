/*
*  @(#)AuthUseCaseConfig.java
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
package br.com.jtech.tasklist.config.usecases;

import br.com.jtech.tasklist.application.core.usecases.AuthUseCase;
import br.com.jtech.tasklist.application.ports.input.AuthInputGateway;
import br.com.jtech.tasklist.application.ports.output.AuthOutputGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* class AuthUseCaseConfig 
* 
* @author jtech
*/
@Configuration
public class AuthUseCaseConfig {

    @Bean
    public AuthInputGateway authInputGateway(AuthOutputGateway authOutputGateway, PasswordEncoder passwordEncoder) {
        return new AuthUseCase(authOutputGateway, passwordEncoder);
    }
}


