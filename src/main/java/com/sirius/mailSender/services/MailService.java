package com.sirius.mailSender.services;

import com.sirius.mailSender.dtos.MailDTO;

import java.util.List;

public interface MailService {
    List<MailDTO> findAll();
    List<MailDTO> findMailsByDate();
}
