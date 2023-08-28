package com.example.studentmanagementsystem.servicetesting;

import com.example.studentmanagementsystem.dto.StudentRequestDto;
import com.example.studentmanagementsystem.enums.Gender;
import com.example.studentmanagementsystem.enums.Status;
import com.example.studentmanagementsystem.exception.EmailAlreadyExistsException;
import com.example.studentmanagementsystem.exception.IdNotValidException;
import com.example.studentmanagementsystem.exception.InvalidDataException;
import com.example.studentmanagementsystem.exception.ResourceNotFoundException;
import com.example.studentmanagementsystem.model.Address;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repo.AddressRepo;
import com.example.studentmanagementsystem.repo.StudentRepo;
import com.example.studentmanagementsystem.serviceimpl.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StudentServiceImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private StudentRepo studentRepo;
   /* @Mock
    private PasswordEncoder passwordEncoder;*/
    @Mock
    private AddressRepo addressRepo;
    @InjectMocks
    private StudentServiceImpl studentService;

    public StudentServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStudents() {
        List<Address> addressList = new ArrayList<>();
        addressList.add(new Address(1, "benepa", "nala"));
        addressList.add(new Address(2, "benep", "nal"));

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "badsna", "badsnastha@gmail.com","badsna123", Gender.FEMALE, 21, Status.ACTIVE, addressList.get(0)));
        studentList.add(new Student(2, "adsna", "adsnastha@gmail.com","adsna123", Gender.FEMALE, 20, Status.DEACTIVE, addressList.get(1)));

        when(studentRepo.findAll()).thenReturn(studentList);
        assertEquals(2, studentService.getAllStudents().size());
        verify(studentRepo, times(1)).findAll();
    }

    @Test
    public void testGetStudentByIdWithInvalidData() {
        assertThrows(IdNotValidException.class, () -> studentService.getStudentById(-1));
    }

    @Test
    public void testGetStudentByIdWithIdDoesntExists() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(5));
    }

    @Test
    public void testGetStudentByIdWithIdExists() {
        Address address = new Address(1, "benepa", "nala");
        Student student = new Student(1, "badsna", "badsnastha@gmail.com","badsna123", Gender.FEMALE, 21, Status.ACTIVE, address);

        when(studentRepo.findById(1)).thenReturn(Optional.of(student));
        //Deep Check==>checks the value inside the object
        //assertThat(student, samePropertyValuesAs(studentService.getStudentById(1)));

        //Shallow Check==>checks the reference of the object
        assertEquals(student.getName(),studentService.getStudentById(1).getName());
    }

    @Test
    public void testAddNewStudentWithEmailAlreadyExists() {
        Address oldAddress = new Address(1, "benepa", "nala");
        StudentRequestDto oldStudent = new StudentRequestDto("badsna", "badsnastha@gmail.com", Gender.FEMALE, "badsna123",12, oldAddress);

        Address newAddress = new Address(1, "benepa", "nala");
        StudentRequestDto newStudent = new StudentRequestDto("badsna", "badsnastha@gmail.com", Gender.FEMALE, "badsna123",21, newAddress);

        Student mockedStudent = new Student();
        when(studentRepo.findByEmail(newStudent.getEmail())).thenReturn(Optional.of(mockedStudent));

        assertThrows(EmailAlreadyExistsException.class, () -> studentService.addNewStudent(newStudent));
    }


    @Test
    public void testAddNewStudentWithEmptyNameField() {
        Address newAddress = new Address(1, "benepa", "nala");
        StudentRequestDto newStudent = new StudentRequestDto("", "badsnastha@gmail.com", Gender.FEMALE, "badsna123",21, newAddress);

        assertThrows(InvalidDataException.class, () -> studentService.addNewStudent(newStudent));
    }

    @Test
    public void testAddNewStudentWithNullNameField() {
        Address newAddress = new Address(1, "benepa", "nala");
        StudentRequestDto newStudent = new StudentRequestDto(null, "badsnastha@gmail.com", Gender.FEMALE, "badsna123", 21,newAddress);

        assertThrows(InvalidDataException.class, () -> studentService.addNewStudent(newStudent));
    }

    @Test
    public void testAddNewStudentWithValidData() {
        Address newAddress = new Address(1, "benepa", "nala");
        StudentRequestDto newStudent = new StudentRequestDto("badsna", "badsnastha@gmail.com", Gender.FEMALE, "badsna123",21, newAddress);

        studentService.addNewStudent(newStudent);

        //verify(studentRepo, times(1)).save(any());

        //the verify method is used to verify that specific methods on mock objects were called with the expected arguments and the expected number of times during testing.
        //so while saving i cant pass the whole object and verify as the method of service layer returns void so i am using ArgumentCaptor
        //The ArgumentCaptor is a powerful tool in testing frameworks like Mockito that allows you to capture
        // and inspect the arguments passed to methods during the execution of your tests.

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepo, times(1)).save(studentArgumentCaptor.capture());

    }

    @Test
    public void testUpdateStudentWithIdDoesntExits() {
        Address updateAddress = new Address(1, "benep", "nal");
        StudentRequestDto updateStudent = new StudentRequestDto("adsna", "adsna@gmail.com", Gender.FEMALE, "adsna123", 20,updateAddress);
        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1, updateStudent));
    }

    @Test
    public void testUpdateStudentWithIdExists() {
        Address existingAddress = new Address(1, "benepa", "nala");
        Student existingStudent = new Student(1, "badsna", "badsnastha@gmail.com","badsna123", Gender.FEMALE, 21, Status.ACTIVE, existingAddress);

        when(studentRepo.getStudentByIdAndStatus(1)).thenReturn(Optional.of(existingStudent));

        Address updateAddress = new Address(1, "benep", "nal");
        StudentRequestDto updateStudent = new StudentRequestDto("adsna", "adsna@gmail.com", Gender.FEMALE, "badsna123",21, updateAddress);

        studentService.updateStudent(1, updateStudent);
        verify(studentRepo, times(1)).save(any());

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepo, times(1)).save(studentArgumentCaptor.capture());

        assertEquals("adsna", studentArgumentCaptor.getValue().getName());
    }

    @Test
    public void testDeleteStudentWithInvalidId() {
        assertThrows(IdNotValidException.class, () -> studentService.deleteStudent(-1));
    }

    @Test
    public void testDeleteStudentWithIdDoesntExists() {
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1));
    }

    @Test
    public void testDeleteStudentWithIdExists() {
        Address existingAddress = new Address(1, "benepa", "nala");
        Student existingStudent = new Student(1, "badsna", "badsnastha@gmail.com","badsna123", Gender.FEMALE, 21, Status.ACTIVE, existingAddress);

        when(studentRepo.findById(1)).thenReturn(Optional.of(existingStudent));

        studentService.deleteStudent(1);
        verify(studentRepo, times(1)).save(any());

      /*  //use @Spy in studentService for this
          //@Spy annotation is used to create a partial mock of a real object.
        Mockito.verify(studentService,times(1)).deleteStudent(1);*/

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepo, times(1)).save(studentArgumentCaptor.capture());

        assertEquals(Status.DEACTIVE, studentArgumentCaptor.getValue().getStatus());

    }
}
