package com.sirius.mailSender.services;

import com.sirius.mailSender.dtos.LoginDTO;
import com.sirius.mailSender.dtos.RegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    void register (@RequestBody RegisterDTO registerDTO);
    String login (@RequestBody LoginDTO loginDTO);
}
