package com.spring.gateway.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.gateway.management.entity.Role;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
