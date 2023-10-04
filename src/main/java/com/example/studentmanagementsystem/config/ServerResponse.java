package com.example.studentmanagementsystem.config;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse<T> {
    private String messgae;
    private T data;
    private HttpStatus httpStatus;
    LocalDateTime createdDate;

    @PrePersist  //method annotated with this will be executed before data is saved in db
    public void createdDate() {
        this.createdDate = LocalDateTime.now(); //set the value of createdDate to LocalDateTime.now();
    }
}
