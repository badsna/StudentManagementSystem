package com.example.studentmanagementsystem.serviceimpl;

import com.example.studentmanagementsystem.config.SecurityConfig;
import com.example.studentmanagementsystem.exception.ResourceNotFoundException;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repo.StudentRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Slf4j
//this class is to be done only for JPA security not for inMemory
public class StudentDetails implements UserDetailsService {
    @Autowired
    private  StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                student.getEmail(), // username
                student.getPassword(), // encoded password
                student.getAuthorities() // authorities/roles
        );
    }
}
