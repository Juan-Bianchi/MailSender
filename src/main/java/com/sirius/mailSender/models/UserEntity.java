package com.sirius.mailSender.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Integer id;
    private String email;
    private String userName;
    private String password;
    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private Set<MailEntity> mailEntities = new HashSet<>();
    private Integer sentEmails;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.sentEmails = 0;
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

    public String getPassword() {
        return password;
    }

    public Set<MailEntity> getMails() {
        return mailEntities;
    }

    public Integer getSentEmails() {
        return sentEmails;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMails(Set<MailEntity> mailEntities) {
        this.mailEntities = mailEntities;
    }

    public void setSentEmails(Integer sentEmails) {
        this.sentEmails = sentEmails;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addAnEMail(MailEntity mailEntity) {
        this.mailEntities.add(mailEntity);
        mailEntity.setSender(this);
    }
}
