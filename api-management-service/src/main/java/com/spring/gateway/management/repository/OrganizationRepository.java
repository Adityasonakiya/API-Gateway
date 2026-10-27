package com.spring.gateway.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.gateway.management.entity.Organization;
import com.spring.gateway.management.entity.OrganizationStatus;

public interface OrganizationRepository extends JpaRepository<Organization,Long>{
    
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlugIgnoreCase(String slug);

    List<Organization> findAllByStatusNot(OrganizationStatus status);

    Optional<Organization> findByIdAndStatusNot(Long id, OrganizationStatus status);
}
