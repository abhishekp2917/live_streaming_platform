package org.example.exception;

public class VideoFileEmptyException extends RuntimeException {
    public VideoFileEmptyException(String message) {
        super(message);
    }
}
