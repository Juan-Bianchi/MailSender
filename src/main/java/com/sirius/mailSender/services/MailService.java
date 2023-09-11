package com.sirius.mailSender.services;

import com.sirius.mailSender.dtos.MailSentDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MailService {
    List<MailSentDTO> findAll();
    List<MailSentDTO> findMailsByDate();
    void sendMail(MailSentDTO mailSentDTO) throws MessagingException, UnsupportedEncodingException;
}
