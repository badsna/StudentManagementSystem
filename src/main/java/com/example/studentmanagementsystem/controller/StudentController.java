package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.ChangePasswordRequestDto;
import com.example.studentmanagementsystem.dto.StudentRequestDto;
import com.example.studentmanagementsystem.dto.StudentResponseDto;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable("id") int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping("/addNew")
    public ResponseEntity<String> addNewStudent(@RequestBody StudentRequestDto studentRequestDto) {
        studentService.addNewStudent(studentRequestDto);
        return ResponseEntity.ok("Student Added Successfully");
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable("id") int id, @RequestBody StudentRequestDto studentRequestDto) {
        studentService.updateStudent(id, studentRequestDto);
        return ResponseEntity.ok("Data of student updated sucessfully");
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/changePassword/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable("id") int id, @RequestBody ChangePasswordRequestDto changePasswordRequestDto){
        studentService.updatePassword(id,changePasswordRequestDto);
        return ResponseEntity.ok("Password Changed Successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping("/sortData/{field}")
    public ResponseEntity<List<StudentResponseDto>> sortStudentBasedOnField(@PathVariable("field") String field) {
        return ResponseEntity.ok(studentService.sortStudentBasedOnField(field));
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public List<Student> getStudentsWithPagination(@PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        Page<Student> students = studentService.getStudentsWithPagination(offset, pageSize);
        List<Student> studentList=students.getContent();
        return studentList;
    }
}
