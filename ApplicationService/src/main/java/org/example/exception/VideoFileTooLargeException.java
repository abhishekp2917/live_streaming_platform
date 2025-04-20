package org.example.exception;

public class VideoFileTooLargeException extends RuntimeException {
    public VideoFileTooLargeException(String message) {
        super(message);
    }
}
