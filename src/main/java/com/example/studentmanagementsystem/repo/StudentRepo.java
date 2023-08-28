package com.example.studentmanagementsystem.repo;

import com.example.studentmanagementsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Integer> {
    @Query(value = "select * from student where student_id=?1 and status='ACTIVE'",nativeQuery = true)
    Optional<Student> getStudentByIdAndStatus(Integer id);

    Optional<Student> findByEmail(String email);
    //Optional<Student> findByName(String name);
}
