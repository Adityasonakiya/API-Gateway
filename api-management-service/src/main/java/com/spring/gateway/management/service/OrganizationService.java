package com.spring.gateway.management.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.gateway.management.dto.requestDto.OrganizationRequest;
import com.spring.gateway.management.dto.requestDto.OrganizationStatusRequest;
import com.spring.gateway.management.dto.requestDto.TenantAdminRequest;
import com.spring.gateway.management.dto.responseDto.OrganizationResponse;
import com.spring.gateway.management.dto.responseDto.TenantAdminResponse;
import com.spring.gateway.management.entity.ManagementUser;
import com.spring.gateway.management.entity.Organization;
import com.spring.gateway.management.entity.OrganizationStatus;
import com.spring.gateway.management.entity.Role;
import com.spring.gateway.management.exception.ApiException;
import com.spring.gateway.management.repository.ManagementUserRepository;
import com.spring.gateway.management.repository.OrganizationRepository;
import com.spring.gateway.management.repository.RoleRepository;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ManagementUserRepository managementUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public OrganizationService(OrganizationRepository organizationRepository,
            ManagementUserRepository managementUserRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder

    ) {
        this.organizationRepository = organizationRepository;
        this.managementUserRepository = managementUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
    public OrganizationResponse changeStatus(Long id, OrganizationStatusRequest request) {
        if (request.getStatus().equals(OrganizationStatus.DELETED)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Use DELETE to soft-delete an organization");
        }

        Organization organization = getOrganization(id);

        organization.setStatus(request.getStatus());
        organization.setUpdatedAt(LocalDateTime.now());

        return OrganizationResponse.from(organization);
    }

    @Transactional
    public void delete(Long id) {
        Organization organization = getOrganization(id);

        organization.setStatus(OrganizationStatus.DELETED);
        organization.setDeletedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public TenantAdminResponse createTenantAdmin(Long organizationId, TenantAdminRequest request) {
        Organization organization = getOrganization(organizationId);

        if (managementUserRepository.findByOrganizationId(organizationId).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "This organization already has a tenant administrator");
        }

        if (managementUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }

        Role role = roleRepository.findByName("TENANT_ADMIN")
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "TENANT_ADMIN role is missing"));
        
        ManagementUser user=new ManagementUser();
        user.setOrganization(organization);
        user.setRole(role);
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        managementUserRepository.save(user);

        return TenantAdminResponse.from(user);
    }

    @Transactional(readOnly = true)
    public TenantAdminResponse getTenantAdmin(Long id){
        return TenantAdminResponse.from(getTenantAdminEntity(id));
    }

    @Transactional 
    public TenantAdminResponse updateTenantAdmin(Long organizationId,TenantAdminRequest request){
        ManagementUser user=getTenantAdminEntity(organizationId);

        if(!user.getUsername().equals(request.getUsername()) && managementUserRepository.existsByUsername(user.getUsername())){
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }

        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setEnabled(request.isEnabled());
        user.setUpdatedAt(LocalDateTime.now());

        return TenantAdminResponse.from(user);
    }

    @Transactional
    public void deleteTenantAdmin(Long organizationId){
        ManagementUser user=getTenantAdminEntity(organizationId);

        managementUserRepository.delete(user);
    } 


    private ManagementUser getTenantAdminEntity(Long organizationId) {
        return managementUserRepository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Organization not found"));
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
