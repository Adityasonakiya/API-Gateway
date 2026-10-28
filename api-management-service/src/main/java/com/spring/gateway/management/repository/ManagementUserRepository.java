package com.spring.gateway.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.gateway.management.entity.ManagementUser;
import java.util.List;


public interface ManagementUserRepository extends JpaRepository<ManagementUser,Long>{
    Optional<ManagementUser> findByUsername(String username);

    Optional<ManagementUser> findByOrganizationId(Long organizationId);

    boolean existsByUsername(String username);
}
