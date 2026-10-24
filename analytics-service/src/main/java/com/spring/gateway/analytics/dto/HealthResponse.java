package com.spring.gateway.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor 
@Getter 
@Setter 
public class HealthResponse {
    String serviceName;
    String serviceDescription;
    String status;
}
