package com.nagarro.af.bookingtablesystem.exception;

public class NotUniqueException extends RuntimeException {
    public NotUniqueException() {
    }

    public NotUniqueException(String message) {
        super(message);
    }
}
