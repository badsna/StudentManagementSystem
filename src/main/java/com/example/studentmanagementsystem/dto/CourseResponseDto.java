package com.example.studentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CourseResponseDto implements Serializable {
    private int courseId;

    private String name;
}
