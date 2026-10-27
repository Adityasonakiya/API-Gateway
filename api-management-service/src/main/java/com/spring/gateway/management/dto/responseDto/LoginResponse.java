package com.spring.gateway.management.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
public class LoginResponse {
    
    private String token;
    private String tokenType;
    private long expiresInSeconds;
    private String role;
    private Long organizationId;
}
