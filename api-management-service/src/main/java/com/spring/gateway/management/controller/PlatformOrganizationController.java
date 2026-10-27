package com.spring.gateway.management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.gateway.management.dto.requestDto.OrganizationRequest;
import com.spring.gateway.management.dto.requestDto.OrganizationStatusRequest;
import com.spring.gateway.management.dto.responseDto.OrganizationResponse;
import com.spring.gateway.management.entity.Organization;
import com.spring.gateway.management.service.OrganizationService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/platform/organizations")
public class PlatformOrganizationController {

    private final OrganizationService organizationService;

    public PlatformOrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // CRUD --> organization & tenant admin

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(
            @Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> findAll() {
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(@PathVariable Long id,
            @Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.update(id,request));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrganizationResponse> changeStatus(@PathVariable Long id,
            @Valid @RequestBody OrganizationStatusRequest request) {
        return ResponseEntity.ok(organizationService.changeStatus(id,request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    } 
}
