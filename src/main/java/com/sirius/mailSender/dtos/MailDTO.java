package com.sirius.mailSender.dtos;

import com.sirius.mailSender.models.Mail;
import com.sirius.mailSender.models.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class MailDTO {

    private Integer id;
    private UserEntity sender;
    private String subject;
    private String message;
    private List<String> recipients;
    private LocalDateTime date;
    private List<String> cc;
    private List<String> bcc;

    public MailDTO(Mail mail) {
        this.id = mail.getId();
        this.sender = mail.getSender();
        this.subject = mail.getSubject();
        this.message = mail.getMessage();
        this.recipients = mail.getRecipients();
        this.date = mail.getDate();
        this.cc = mail.getCc();
        this.bcc = mail.getBcc();
    }

    public Integer getId() {
        return id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }
}
