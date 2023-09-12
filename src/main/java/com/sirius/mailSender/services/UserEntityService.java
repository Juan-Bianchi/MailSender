package com.sirius.mailSender.services;

import com.sirius.mailSender.models.UserEntity;

import java.util.Set;

public interface UserEntityService {
    Set<com.sirius.mailSender.dtos.UserEntityDTO> findAll();
    void register(String email, String userName, String password);
    UserEntity findUserEntityByUserName(String userName);
    Boolean existsByUserName(String userName);
}
