package org.example.persistence;

import org.example.exception.InvalidUsernameOrPasswordException;
import org.example.model.User;
import org.example.properties.JWTProperties;
import org.example.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.service.IPasswordEncoder;
import java.util.UUID;

@Service
public class MySQLUserDbServiceImpl implements IUserDbService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPasswordEncoder passwordEncoder;

    @Autowired
    private JWTProperties jwtProperties;

    public User createUser(User inputUser) {
        if(inputUser.getUsername()==null || inputUser.getPassword()==null) {
            throw new IllegalArgumentException("Username and Password can't be null.");
        }
        inputUser.setUserId(UUID.randomUUID().toString());
        String encodedPassword = this.passwordEncoder.encode(inputUser.getPassword());
        inputUser.setPassword(encodedPassword);
        inputUser.setScope(this.jwtProperties.getScope());
        return this.userRepository.save(inputUser);
    }

    public User authenticateUser(User inputUser) {
        if(inputUser.getUsername()==null || inputUser.getPassword()==null) {
            throw new IllegalArgumentException("Username and Password can't be null.");
        }
        if(inputUser.getScope()==null) {
            throw new IllegalArgumentException("Scope can't be null.");
        }
        User storedUser = userRepository.findByUsername(inputUser.getUsername())
                .orElseThrow(() -> new InvalidUsernameOrPasswordException("User not found"));
        if (inputUser.getScope().compareTo(storedUser.getScope())!=0) {
            throw new InvalidUsernameOrPasswordException("Invalid scope");
        }
        if (!passwordEncoder.matches(inputUser.getPassword(), storedUser.getPassword())) {
            throw new InvalidUsernameOrPasswordException("Invalid password");
        }
        return storedUser;
    }
}

