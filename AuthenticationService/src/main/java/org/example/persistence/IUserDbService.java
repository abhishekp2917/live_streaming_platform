package org.example.persistence;

import org.example.model.User;

public interface IUserDbService {

    User createUser(User inputUser);

    User authenticateUser(User inputUser);
}
