package com.spring.gateway.management.dto.requestDto;

import com.spring.gateway.management.entity.OrganizationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter 
@Getter 
public class OrganizationStatusRequest {
    
    @NotNull 
    private OrganizationStatus status;
}
