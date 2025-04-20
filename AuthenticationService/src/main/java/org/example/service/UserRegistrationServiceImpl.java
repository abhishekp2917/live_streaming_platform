package org.example.service;

import org.example.dto.*;
import org.example.model.User;
import org.example.persistence.IUserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements IUserRegistrationService <IRegistrationResponseDTO, UserDTO>{

    @Autowired
    private IUserDbService userDbService;

    @Override
    public IRegistrationResponseDTO register(UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();
        User createdUser = userDbService.createUser(user);
        IRegistrationResponseDTO responseDTO = SuccessfulRegistrationResponseDTO.builder()
                .userId(createdUser.getUserId())
                .username(createdUser.getUsername())
                .scope(createdUser.getScope())
                .build();
        return responseDTO;
    }
}
