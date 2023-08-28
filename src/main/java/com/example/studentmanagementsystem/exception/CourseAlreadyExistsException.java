package com.example.studentmanagementsystem.exception;

public class CourseAlreadyExistsException extends RuntimeException{
    public CourseAlreadyExistsException(String message) {
        super(message);
    }
}
