package com.spring.gateway.management.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HealthService {

    private final JdbcTemplate jdbcTemplate;

    public String getHealth() {
        String dbStatus = "DOWN";

        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (Integer.valueOf(result).equals(1)) {
                dbStatus = "UP";
            }
        } catch (Exception e) {
            System.out.println("MySql health check failed: " + e.getMessage());
        }

        return dbStatus;
    }
}
