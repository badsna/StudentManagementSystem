package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.dto.ChangePasswordRequestDto;
import com.example.studentmanagementsystem.dto.StudentRequestDto;
import com.example.studentmanagementsystem.dto.StudentResponseDto;
import com.example.studentmanagementsystem.model.Student;
import org.springframework.data.domain.Page;

import java.util.List;


public interface StudentService {
    public List<StudentResponseDto> getAllStudents();

    StudentResponseDto getStudentById(int id);

    void addNewStudent(StudentRequestDto studentRequestDto);

    void updateStudent(int id, StudentRequestDto studentRequestDto);

    void deleteStudent(int id);

    List<StudentResponseDto> sortStudentBasedOnField(String field);

    Page<Student> getStudentsWithPagination(int offset, int pageSize);

    void updatePassword(int id, ChangePasswordRequestDto changePasswordRequestDto);
}
