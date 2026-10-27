package com.spring.gateway.management.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.gateway.management.dto.requestDto.OrganizationRequest;
import com.spring.gateway.management.dto.requestDto.OrganizationStatusRequest;
import com.spring.gateway.management.dto.responseDto.OrganizationResponse;
import com.spring.gateway.management.entity.Organization;
import com.spring.gateway.management.entity.OrganizationStatus;
import com.spring.gateway.management.exception.ApiException;
import com.spring.gateway.management.repository.OrganizationRepository;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public OrganizationResponse create(OrganizationRequest request) {

        checkUnique(request.getName(), request.getSlug(), null);

        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setSlug(request.getSlug());
        organization.setStatus(OrganizationStatus.PENDING);
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());

        organizationRepository.save(organization);

        return OrganizationResponse.from(organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAll() {
        List<OrganizationResponse> responses = new ArrayList<>();

        for (Organization org : organizationRepository.findAllByStatusNot(OrganizationStatus.DELETED)) {
            responses.add(OrganizationResponse.from(org));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public OrganizationResponse findById(Long id) {
        return OrganizationResponse.from(getOrganization(id));
    }

    @Transactional
    public OrganizationResponse update(Long id, OrganizationRequest request) {
        checkUnique(request.getName(), request.getSlug(), id);

        Organization organization = getOrganization(id);

        organization.setName(request.getName());
        organization.setSlug(request.getSlug());
        organization.setUpdatedAt(LocalDateTime.now());

        return OrganizationResponse.from(organization);
    }

    @Transactional
    public OrganizationResponse changeStatus(Long id,OrganizationStatusRequest request) {
        if (request.getStatus().equals(OrganizationStatus.DELETED)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Use DELETE to soft-delete an organization");
        }

        Organization organization=getOrganization(id);

        organization.setStatus(request.getStatus());
        organization.setUpdatedAt(LocalDateTime.now());

        return OrganizationResponse.from(organization);
    }

    @Transactional 
    public void delete(Long id){
        Organization organization=getOrganization(id);

        organization.setStatus(OrganizationStatus.DELETED);
        organization.setDeletedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());
    }


    private Organization getOrganization(Long id) {
        return organizationRepository.findByIdAndStatusNot(id, OrganizationStatus.DELETED)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Organization not found"));
    }

    private void checkUnique(String name, String slug, Long currentId) {
        if (organizationRepository.existsByNameIgnoreCase(name)) {
            Organization current = currentId == null ? null : organizationRepository.findById(currentId).orElse(null);

            if (current == null || !current.getName().equalsIgnoreCase(name)) {
                throw new ApiException(HttpStatus.CONFLICT, "Organization name already exists");
            }
        }

        if (organizationRepository.existsBySlugIgnoreCase(slug)) {
            Organization current = currentId == null ? null : organizationRepository.findById(currentId).orElse(null);

            if (current == null || !current.getSlug().equalsIgnoreCase(slug)) {
                throw new ApiException(HttpStatus.CONFLICT, "Organization slug already exists");
            }
        }
    }
}
