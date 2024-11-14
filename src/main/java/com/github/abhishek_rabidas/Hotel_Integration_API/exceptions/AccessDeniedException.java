package com.github.abhishek_rabidas.Hotel_Integration_API.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {}

    public AccessDeniedException(String message) {
        super(message);
    }
}
