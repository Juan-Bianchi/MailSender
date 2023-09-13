package com.sirius.mailSender.controllers;

import com.sirius.mailSender.dtos.UserEntityDTO;
import com.sirius.mailSender.services.UserEntityService;
import com.sirius.mailSender.services.MailService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final MailService mailService;

    public UserEntityController(UserEntityService userEntityService, MailService mailService) {
        this.userEntityService = userEntityService;
        this.mailService = mailService;
    }

    @GetMapping("/userEntities")
    public Set<UserEntityDTO> getMailOwners(){
        return this.userEntityService.findAll();
    }
}
