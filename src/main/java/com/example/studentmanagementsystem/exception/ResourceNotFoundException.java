package com.example.studentmanagementsystem.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
