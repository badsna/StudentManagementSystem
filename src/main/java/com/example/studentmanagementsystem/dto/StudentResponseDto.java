package com.example.studentmanagementsystem.dto;

import com.example.studentmanagementsystem.enums.Gender;
import com.example.studentmanagementsystem.enums.Status;
import com.example.studentmanagementsystem.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto implements Serializable {
    private int studentId;
    private String name;
    private String email;
    private Gender gender;
    private int age;
    private Status status;

    private String password;

    private Address address;

}
