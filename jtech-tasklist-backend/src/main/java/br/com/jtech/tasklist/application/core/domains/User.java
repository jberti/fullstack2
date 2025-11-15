/*
*  @(#)User.java
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
package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* class User 
* 
* @author jtech
*/
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User of(UserEntity entity) {
        return User.builder()
            .id(entity.getId().toString())
            .name(entity.getName())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public UserEntity toEntity() {
        UserEntity.UserEntityBuilder builder = UserEntity.builder()
            .name(this.name)
            .email(this.email)
            .password(this.password);
        
        if (this.id != null) {
            builder.id(UUID.fromString(this.id));
        }
        
        return builder.build();
    }
}

