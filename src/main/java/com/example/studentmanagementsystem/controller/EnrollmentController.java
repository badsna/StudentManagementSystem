package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("enroll")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<String> enrollment(@RequestParam("studentId") int studentId, @RequestParam int courseId) throws Exception {

        System.out.println(courseId);
        enrollmentService.enrollment(studentId,courseId);
        return ResponseEntity.ok("Student with id "+studentId+" has been enrolled in course with id "+courseId);
    }
}
