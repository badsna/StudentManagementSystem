package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.dto.StudentRequestDto;
import com.example.studentmanagementsystem.dto.StudentResponseDto;
import com.example.studentmanagementsystem.enums.Gender;
import com.example.studentmanagementsystem.enums.Status;
import com.example.studentmanagementsystem.model.Address;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repo.StudentRepo;
import com.example.studentmanagementsystem.serviceimpl.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc

class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentServiceImpl studentService;


    @Test
    @WithMockUser(authorities = "USER")
    public void testGetAllStudents() throws Exception {
        List<Address> addressList = new ArrayList<>();
        addressList.add(new Address(1, "benepa", "nala"));
        addressList.add(new Address(2, "benep", "nal"));

        List<StudentResponseDto> studentList = new ArrayList<>();
        studentList.add(new StudentResponseDto(1, "badsna", "badsnastha@gmail.com", Gender.FEMALE, 21, Status.ACTIVE, "badsna123", addressList.get(0)));
        studentList.add(new StudentResponseDto(2, "adsna", "adsnastha@gmail.com", Gender.FEMALE, 20, Status.DEACTIVE, "adsna123", addressList.get(1)));

        when(studentService.getAllStudents()).thenReturn(studentList);
        mockMvc.perform(get("http://localhost:8080/student/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    @WithMockUser
    public void testGetStudentById() throws Exception {
        Address address = new Address(1, "benepa", "nala");
        StudentResponseDto student = new StudentResponseDto(1, "badsna", "badsnastha@gmail.com", Gender.FEMALE, 21, Status.ACTIVE, "badsna123", address);
        when(studentService.getStudentById(1)).thenReturn(student);
        mockMvc.perform(get("http://localhost:8080/student/getById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser()
    public void testAddNewStudent() throws Exception {
        Address newAddress = new Address(1, "benepa", "nala");
        StudentRequestDto newStudent = new StudentRequestDto("rujan", "rujanstha@gmail.com", Gender.FEMALE, "badsna123", 21, newAddress);

        mockMvc.perform(post("/student/addNew")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newStudent))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Student Added Successfully"));
    }


    @Test
    @WithMockUser(authorities = "USER")
    public void testUpdateStudent() throws Exception {
        Address existingAddress = new Address(1, "benepa", "nala");
        Student existingStudent = new Student(1, "badsna", "badsnastha@gmail.com", "badsna123", Gender.FEMALE, 21, Status.ACTIVE, existingAddress);

        Address updateAddress = new Address(1, "benep", "nal");
        StudentRequestDto updateStudent = new StudentRequestDto("adsna", "adsna@gmail.com", Gender.FEMALE, "badsna123", 21, updateAddress);

        mockMvc.perform(put("http://localhost:8080/student/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateStudent))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("http://localhost:8080/student/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
               .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));

    }


}