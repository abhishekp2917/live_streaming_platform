package org.example.service;

public interface IUserRegistrationService<R, T> {

    R register(T inputUser);
}
