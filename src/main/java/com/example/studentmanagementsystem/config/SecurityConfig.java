package com.example.studentmanagementsystem.config;

import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.serviceimpl.StudentDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private StudentDetails studentDetails;
    @Autowired
    private PasswordEncoder encoder;

    /*
    log.info("for inMemory");
    @Bean
    public UserDetailsService userDetailsService() {
        log.info("inside secutiyconfig>>userdetailsservice");
        UserDetails admin = User
                .builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails users = User
                .builder()
                .username("user")
                .password(encoder.encode("user123"))
                .roles("USER")
                .build();
        log.info("creating admin and user");
        return new InMemoryUserDetailsManager(admin, users);
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /*log.info("For inmemory");
        return httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/course/addNew", "/student/delete/{id:\\d+}", "/student/getAll").hasRole("ADMIN")
                .requestMatchers("/student/update/{id:\\d+}").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .build();*/
        log.info("For JPA Security");
        return httpSecurity.csrf().disable()
                .userDetailsService(studentDetails)
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .build();

    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        log.info("inside password encoder");
        return new BCryptPasswordEncoder();
    }
}
