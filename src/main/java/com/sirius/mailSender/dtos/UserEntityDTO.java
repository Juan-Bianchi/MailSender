package com.sirius.mailSender.dtos;

import com.sirius.mailSender.models.Mail;
import com.sirius.mailSender.models.UserEntity;

import java.util.Set;

public class UserEntityDTO {

    private Integer id;
    private String email;
    private String userName;
    private Set<Mail> mails;
    private Integer sentEmails;

    public UserEntityDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.userName = userEntity.getUserName();
        this.mails = userEntity.getMails();
        this.sentEmails = userEntity.getSentEmails();
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

    public Set<Mail> getMails() {
        return mails;
    }

    public Integer getSentEmails() {
        return sentEmails;
    }
}
