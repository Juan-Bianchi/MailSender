package com.sirius.mailSender.dtos;

import com.sirius.mailSender.models.MailEntity;
import com.sirius.mailSender.models.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserEntityDTO {

    private Integer id;
    private String email;
    private String userName;
    private Set<MailEntity> mailEntities;
    private Integer sentEmails;
    private List<RoleDTO> roles;

    public UserEntityDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.userName = userEntity.getUserName();
        this.mailEntities = userEntity.getMails();
        this.sentEmails = userEntity.getSentEmails();
        this.roles = userEntity.getRoles().stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public Set<MailEntity> getMails() {
        return mailEntities;
    }

    public Integer getSentEmails() {
        return sentEmails;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
