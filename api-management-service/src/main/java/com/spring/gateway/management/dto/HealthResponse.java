package com.spring.gateway.management.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor 
@Getter 
@Setter 
public class HealthResponse {
    private String serviceStatus;
    private String dbStatus;
    private LocalDateTime timeStamp;
}

