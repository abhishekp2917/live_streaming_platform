package org.example.service;

public interface IPasswordEncoder {

    String encode(String password);

    boolean matches(String rawPassword, String encodedPassword);
}
