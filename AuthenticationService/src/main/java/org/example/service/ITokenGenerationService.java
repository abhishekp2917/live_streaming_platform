package org.example.service;

public interface ITokenGenerationService <T> {

    String generateToken(T user);
}
