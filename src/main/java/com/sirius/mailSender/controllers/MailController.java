package com.sirius.mailSender.controllers;

import com.sirius.mailSender.dtos.MailSentDTO;
import com.sirius.mailSender.services.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/mails/send")
    public ResponseEntity<Object> sendMail (@RequestBody MailSentDTO mailSentDTO) {
        try {
            mailService.sendMail(mailSentDTO);
            return new ResponseEntity<>("Registered", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}
