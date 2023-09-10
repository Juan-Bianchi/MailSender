package com.sirius.mailSender.controllers;

import com.sirius.mailSender.dtos.UserEntityDTO;
import com.sirius.mailSender.services.UserEntityService;
import com.sirius.mailSender.services.MailService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final MailService mailService;

    public UserEntityController(UserEntityService userEntityService, @Lazy MailService mailService) {
        this.userEntityService = userEntityService;
        this.mailService = mailService;
    }

    @GetMapping("/mailOwners")
    public Set<UserEntityDTO> getMailOwners(){
        return this.userEntityService.findAll();
    }

    @PostMapping("/mailOwners")
    public ResponseEntity<Object> register (@RequestParam String email, @RequestParam String firstName,
                                                  @RequestParam String lastName, @RequestParam String password) {
        try {
            userEntityService.register(email, firstName, lastName, password);
            return new ResponseEntity<>("Registered", HttpStatus.CREATED);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


}