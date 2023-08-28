package com.example.studentmanagementsystem.serviceimpl;

import com.example.studentmanagementsystem.dto.ChangePasswordRequestDto;
import com.example.studentmanagementsystem.dto.StudentRequestDto;
import com.example.studentmanagementsystem.dto.StudentResponseDto;
import com.example.studentmanagementsystem.enums.Role;
import com.example.studentmanagementsystem.enums.Status;
import com.example.studentmanagementsystem.exception.*;
import com.example.studentmanagementsystem.model.Address;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repo.AddressRepo;
import com.example.studentmanagementsystem.repo.StudentRepo;
import com.example.studentmanagementsystem.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepo studentRepo;
    private final AddressRepo addressRepo;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;


    public static StudentResponseDto convertToStudentResponseDto(Student student) {
        StudentResponseDto studentResponseDto = new StudentResponseDto();
        studentResponseDto.setStudentId(student.getStudentId());
        studentResponseDto.setName(student.getName());
        studentResponseDto.setEmail(student.getEmail());
        studentResponseDto.setPassword(student.getPassword());
        studentResponseDto.setGender(student.getGender());
        studentResponseDto.setStatus(student.getStatus());
        studentResponseDto.setAge(student.getAge());
        studentResponseDto.setAddress(student.getAddress());

        return studentResponseDto;
    }

    private static Student convertToStudent(StudentRequestDto studentRequestDto) {
        Student student = new Student();
        student.setName(studentRequestDto.getName());
        student.setEmail(studentRequestDto.getEmail());
        student.setPassword(studentRequestDto.getPassword());
        student.setGender(studentRequestDto.getGender());
        student.setAge(studentRequestDto.getAge());
        student.setAddress(studentRequestDto.getAddress());
        return student;
    }

    @Override
    public List<StudentResponseDto> getAllStudents() {
        List<Student> studentList = studentRepo.findAll();
        List<StudentResponseDto> studentResponseDtos = new ArrayList<>();
        for (Student student : studentList) {
            studentResponseDtos.add(convertToStudentResponseDto(student));
        }
        return studentResponseDtos;
    }

    @Override
    public StudentResponseDto getStudentById(int id) {
        if (id <= 0) {
            throw new IdNotValidException("Given id " + id + " is not valid. Please enter valid id");
        }
        Student student = studentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student with " + id + " doesn't exists"));

        return convertToStudentResponseDto(student);
    }

    @Override
    public void addNewStudent(StudentRequestDto studentRequestDto) {
        Optional<Student> existingStudent = studentRepo.findByEmail(studentRequestDto.getEmail());

        if (existingStudent.isPresent()) {
            throw new EmailAlreadyExistsException("Submitted email already exists. Check the email address again");
        }

        if (studentRequestDto.getName() == null || studentRequestDto.getName().isEmpty()) {
            throw new InvalidDataException("Submitted data is invalid. Please enter valid data");
        }

        Student student = convertToStudent(studentRequestDto);
        student.setStatus(Status.ACTIVE);
        if(student.getEmail().equals("badsnastha@gmail.com")){
            student.setRole(Role.ADMIN);
        }
        else
            student.setRole(Role.USER);
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        studentRepo.save(student);
    }

    @Override
    @Transactional
    public void updateStudent(int id, StudentRequestDto studentRequestDto) {
        Student existingStudent = studentRepo.getStudentByIdAndStatus(id).orElseThrow(() -> new ResourceNotFoundException("Student with " + id + " doesn't exits at present"));

        existingStudent.setName(studentRequestDto.getName());
        existingStudent.setEmail(studentRequestDto.getEmail());
        existingStudent.setGender(studentRequestDto.getGender());
        existingStudent.setAge(studentRequestDto.getAge());

        Address address = studentRequestDto.getAddress();
        Address existingAddress = existingStudent.getAddress();

        existingAddress.setCity(address.getCity());
        existingAddress.setTown(address.getTown());
        addressRepo.save(existingAddress);

        existingStudent.setAddress(existingAddress);
        studentRepo.save(existingStudent);
    }

    @Override
    public void deleteStudent(int id) {
        if (id <= 0) {
            throw new IdNotValidException("Given id " + id + " is not valid. Please enter valid id");
        }
        Student student = studentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student with " + id + " doesn't exists"));
        student.setStatus(Status.DEACTIVE);
        studentRepo.save(student);
    }

    @Override
    public List<StudentResponseDto> sortStudentBasedOnField(String field) {
        List<Student> studentList = studentRepo.findAll(Sort.by(Sort.Direction.ASC,field));
        List<StudentResponseDto> studentResponseDtos = new ArrayList<>();
        for (Student student : studentList) {
            studentResponseDtos.add(convertToStudentResponseDto(student));
        }
        return studentResponseDtos;
    }

    @Override
    public Page<Student> getStudentsWithPagination(int offset, int pageSize) {
        return studentRepo.findAll(PageRequest.of(offset,pageSize));
    }

    @Override
    public void updatePassword(int id, ChangePasswordRequestDto changePasswordRequestDto) {
        Student existingStudent=studentRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student Not Found"));
        boolean student= studentRepo.existsById(id);
        if(student){
            /*//update garda entry garne old password
            String oldPassword= passwordEncoder.encode(changePasswordRequestDto.getOldPassword());*/

            BCryptPasswordEncoder passwordEncoder1=new BCryptPasswordEncoder();
           if( passwordEncoder1.matches(changePasswordRequestDto.getOldPassword(), existingStudent.getPassword()))
           /* if(oldPassword.equals(existingStudent.get().getPassword()))*/{
                log.info("inside if>>if");
                if(changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getRePassword())){
                    log.info("inside if>>if>>if");
                    existingStudent.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
                    studentRepo.save(existingStudent);
                }
                else {
                    log.info("inside else for if>>if>>if");
                    throw new PasswordDoesntMatchException("New Password and Re Password doesnt match. Please try again!!!");
                }
            }
            else {
                log.info("inside else for if>>if");
                throw new PasswordDoesntMatchException("Old Password doesnt matches");
            }
        }

    }
}
