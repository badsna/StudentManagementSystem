package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.CourseRequestDto;
import com.example.studentmanagementsystem.dto.CourseResponseDto;

import java.util.List;

public interface CourseService {
    public List<CourseResponseDto> getAllCourse() ;

    public CourseResponseDto getCourseById(int id) ;

    public void addNewCourse(CourseRequestDto courseRequestDto) ;
}
