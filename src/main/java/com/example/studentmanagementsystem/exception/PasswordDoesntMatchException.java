package com.example.studentmanagementsystem.exception;

public class PasswordDoesntMatchException extends RuntimeException{
    public PasswordDoesntMatchException(String message) {
        super(message);
    }
}
