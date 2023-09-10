package com.sirius.mailSender.services.implementations;

import com.sirius.mailSender.dtos.MailDTO;
import com.sirius.mailSender.services.MailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServiceImplementation implements MailService {


    @Override
    public List<MailDTO> findAll() {
        return null;
    }

    @Override
    public List<MailDTO> findMailsByDate() {
        return null;
    }
}
