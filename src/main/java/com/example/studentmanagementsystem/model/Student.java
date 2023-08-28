package com.example.studentmanagementsystem.model;

import com.example.studentmanagementsystem.enums.Gender;
import com.example.studentmanagementsystem.enums.Role;
import com.example.studentmanagementsystem.enums.Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table

//this implementation is to be done only for JPA security not for inMemory
public class Student implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int age;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "addressId")
    private Address address;

    //this is the parent which manages the json
    @JsonManagedReference()
    @ManyToMany(mappedBy = "students",fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();


    public Student(int studentId, String name, String email,String password, Gender gender, int age, Status status, Address address) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.password=password;
        this.gender = gender;
        this.age = age;
        this.status = status;
        this.address = address;
    }

    public Student(Student student) {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
