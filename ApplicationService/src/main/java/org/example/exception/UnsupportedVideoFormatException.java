package org.example.exception;

public class UnsupportedVideoFormatException extends RuntimeException {
    public UnsupportedVideoFormatException(String message) {
        super(message);
    }
}
