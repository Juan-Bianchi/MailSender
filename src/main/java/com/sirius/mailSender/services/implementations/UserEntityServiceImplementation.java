package com.sirius.mailSender.services.implementations;

import com.sirius.mailSender.dtos.UserEntityDTO;
import com.sirius.mailSender.models.UserEntity;
import com.sirius.mailSender.repositories.UserEntityRepository;
import com.sirius.mailSender.services.UserEntityService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserEntityServiceImplementation implements UserEntityService {

    private final UserEntityRepository userEntityRepository;


    public UserEntityServiceImplementation(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public Set<UserEntityDTO> findAll() {
        return userEntityRepository.findAll().stream().map(UserEntityDTO::new).collect(Collectors.toSet());
    }

    @Override
    public UserEntity findUserEntityByUserName(String email) {
        return userEntityRepository.findByUserName(email).orElse(null);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userEntityRepository.existsByUserName(userName);
    }
}
