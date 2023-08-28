package com.example.studentmanagementsystem.repo;

import com.example.studentmanagementsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course,Integer> {
    Optional<Course> findByName(String name);
}
