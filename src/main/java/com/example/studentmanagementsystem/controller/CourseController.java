package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.CourseRequestDto;
import com.example.studentmanagementsystem.dto.CourseResponseDto;
import com.example.studentmanagementsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/getAll")
    public ResponseEntity<List<CourseResponseDto>> getAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourse());
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getById/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable("id") int id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addNew")
    public ResponseEntity<String> addNewCourse(@RequestBody CourseRequestDto courseRequestDto){
        System.out.println(courseRequestDto.getName());
        courseService.addNewCourse(courseRequestDto);
        return ResponseEntity.ok("Course Added Successfully");
    }

}
