package com.spring.gateway.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@Table(name="roles")
@Entity 
public class Role {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String name;
}
