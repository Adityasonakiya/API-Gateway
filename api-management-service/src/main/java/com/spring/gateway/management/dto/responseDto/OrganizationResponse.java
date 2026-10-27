package com.spring.gateway.management.dto.responseDto;

import java.time.LocalDateTime;

import com.spring.gateway.management.entity.Organization;
import com.spring.gateway.management.entity.OrganizationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrganizationResponse {
    private Long id;
    private String name;
    private String slug;
    private OrganizationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getStatus(),
                organization.getCreatedAt(),
                organization.getUpdatedAt());
    }
}
