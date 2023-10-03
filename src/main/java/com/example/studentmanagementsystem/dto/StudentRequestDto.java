package com.example.studentmanagementsystem.dto;

import com.example.studentmanagementsystem.enums.Gender;
import com.example.studentmanagementsystem.enums.Status;
import com.example.studentmanagementsystem.model.Address;
import com.example.studentmanagementsystem.model.Course;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class StudentRequestDto implements Serializable {
    private String name;
    private String email;
    private Gender gender;
    private String password;
    private int age;
    private Address address;


}
