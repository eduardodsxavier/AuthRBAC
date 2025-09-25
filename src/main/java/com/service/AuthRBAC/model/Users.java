package com.service.AuthRBAC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.service.AuthRBAC.enums.Role;

@Entity
@Table
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    public Users() {};

    public Users(Long id, String name, String password, Role role, boolean enabled) {
        this.id = id;
        this.name = name;
        this.password = password; 
        this.role = role;
        this.enabled = enabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String password() {
        return password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role role() {
        return role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean enabled() {
        return enabled;
    }
}
