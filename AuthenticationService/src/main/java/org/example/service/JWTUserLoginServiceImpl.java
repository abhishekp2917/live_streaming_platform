package org.example.service;

import org.example.dto.ILoginResponseDTO;
import org.example.dto.SuccessfulLoginResponseDTO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.persistence.IUserDbService;
import org.example.properties.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JWTUserLoginServiceImpl implements IUserLoginService <ILoginResponseDTO, UserDTO>{

    @Autowired
    private IUserDbService userDbService;

    @Autowired
    private ITokenGenerationService tokenGenerationService;

    @Autowired
    private JWTProperties jwtProperties;

    @Override
    public ILoginResponseDTO login(UserDTO userDTO) {
        User inputUser = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .scope(userDTO.getScope())
                .build();
        User user = userDbService.authenticateUser(inputUser);
        String jwtToken = tokenGenerationService.generateToken(user);
        ILoginResponseDTO responseDTO = SuccessfulLoginResponseDTO.builder()
                .accessToken(jwtToken)
                .expiresIn(this.jwtProperties.getExpiration())
                .scope(user.getScope())
                .tokenType(this.jwtProperties.getTokenType())
                .build();
        return responseDTO;
    }
}
