package com.sirius.mailSender.dtos;

import com.sirius.mailSender.models.Mail;

import java.time.LocalDateTime;
import java.util.List;

public class MailSentDTO {

    private String sender;
    private String subject;
    private String message;
    private List<String> recipients;
    private LocalDateTime date;
    private List<String> cc;
    private List<String> bcc;

    public MailSentDTO(Mail mail) {
        this.sender = mail.getSender().getUserName();
        this.subject = mail.getSubject();
        this.message = mail.getMessage();
        this.recipients = mail.getRecipients();
        this.cc = mail.getCc();
        this.bcc = mail.getBcc();
    }

    public MailSentDTO(String sender, String subject, String message, List<String> recipients, List<String> cc, List<String> bcc) {
        this.sender = sender;
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;
        this.cc = cc;
        this.bcc = bcc;
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

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }
}
