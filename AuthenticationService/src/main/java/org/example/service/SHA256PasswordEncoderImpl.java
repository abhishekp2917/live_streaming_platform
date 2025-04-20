package org.example.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256PasswordEncoderImpl implements IPasswordEncoder {

    private static final SecureRandom random = new SecureRandom();

    public String encode(String rawPassword) {
        String salt = generateSalt();
        return salt + "$" + hashPassword(rawPassword, salt);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split("\\$");
        String salt = parts[0];
        String storedHash = parts[1];
        return storedHash.equals(hashPassword(rawPassword, salt));
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }

    private String generateSalt() {
        byte[] salt = new byte[16]; // 128-bit salt
        random.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
