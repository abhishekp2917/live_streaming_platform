package org.example.controller;

import org.example.dto.ILoginResponseDTO;
import org.example.dto.IRegistrationResponseDTO;
import org.example.dto.UserDTO;
import org.example.service.IUserLoginService;
import org.example.service.IUserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    @Autowired
    private IUserLoginService <ILoginResponseDTO, UserDTO> userLoginService;

    @Autowired
    private IUserRegistrationService <IRegistrationResponseDTO, UserDTO> userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<IRegistrationResponseDTO> registerUser(@RequestBody UserDTO userDTO) {
        IRegistrationResponseDTO registrationResponseDTO = this.userRegistrationService.register(userDTO);
        return ResponseEntity.ok(registrationResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ILoginResponseDTO> authenticateUser(@RequestBody UserDTO userDTO) {
        ILoginResponseDTO loginResponseDTO = this.userLoginService.login(userDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
