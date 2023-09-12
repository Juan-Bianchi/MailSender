package com.sirius.mailSender.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mailOwner_id")
    private UserEntity sender;
    private String subject;
    private String message;
    @ElementCollection
    private List<String> recipients = new ArrayList<>();
    private LocalDateTime date;


    public MailEntity() {}

    public MailEntity(String subject, String message, List<String> recipients, LocalDateTime date) {
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;
        this.date = date;
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

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
