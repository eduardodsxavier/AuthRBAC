package com.service.AuthRBAC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Users {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;

    private boolean disabled;

    public Users(Long id, String name, String password, Role role, boolean disabled) {
        this.id = id;
        this.name = name;
        this.password = password; 
        this.role = role;
        this.disabled = disabled;
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

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean disabled() {
        return disabled;
    }
}
