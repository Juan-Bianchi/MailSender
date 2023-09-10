package com.sirius.mailSender.dtos;

import com.sirius.mailSender.models.Role;

public class RoleDTO {
    private Long id;
    private String name;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
