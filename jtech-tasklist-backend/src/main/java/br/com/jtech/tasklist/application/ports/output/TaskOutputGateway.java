/*
*  @(#)TaskOutputGateway.java
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
package br.com.jtech.tasklist.application.ports.output;

import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.List;
import java.util.Optional;

/**
* interface TaskOutputGateway 
* 
* @author jtech
*/
public interface TaskOutputGateway {
    
    Task save(Task task);
    
    List<Task> findByUserId(String userId);
    
    Optional<Task> findByIdAndUserId(String id, String userId);
    
    void delete(String id);
    
    boolean existsByIdAndUserId(String id, String userId);
}


