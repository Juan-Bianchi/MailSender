package com.sirius.mailSender.dtos;

import com.sirius.mailSender.models.MailEntity;

import java.time.LocalDateTime;
import java.util.List;

public class MailSentDTO {

    private String sender;
    private String subject;
    private String message;
    private List<String> recipients;
    private LocalDateTime date;

    public MailSentDTO(MailEntity mailEntity) {
        this.sender = mailEntity.getSender().getUserName();
        this.subject = mailEntity.getSubject();
        this.message = mailEntity.getMessage();
        this.recipients = mailEntity.getRecipients();
    }

    public MailSentDTO(String sender, String subject, String message, List<String> recipients, List<String> cc, List<String> bcc) {
        this.sender = sender;
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;
    }


    public String getSender() {
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

}
