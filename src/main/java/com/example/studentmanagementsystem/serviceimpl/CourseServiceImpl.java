package com.example.studentmanagementsystem.serviceimpl;

import com.example.studentmanagementsystem.dto.CourseRequestDto;
import com.example.studentmanagementsystem.dto.CourseResponseDto;
import com.example.studentmanagementsystem.exception.CourseAlreadyExistsException;
import com.example.studentmanagementsystem.exception.IdNotValidException;
import com.example.studentmanagementsystem.exception.InvalidDataException;
import com.example.studentmanagementsystem.exception.ResourceNotFoundException;
import com.example.studentmanagementsystem.model.Course;
import com.example.studentmanagementsystem.repo.CourseRepo;
import com.example.studentmanagementsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepo courseRepo;

    public static CourseResponseDto convertToCourseResponseDto(Course course) {
        CourseResponseDto courseResponseDto = new CourseResponseDto();
        courseResponseDto.setCourseId(course.getCourseId());
        courseResponseDto.setName(course.getName());
        return courseResponseDto;
    }

    private static Course convertToCourse(CourseRequestDto courseRequestDto) {
        Course course = new Course();
        course.setName(courseRequestDto.getName());
        return course;
    }

    @Override
    public List<CourseResponseDto> getAllCourse() {
        List<Course> courseList = courseRepo.findAll();
        List<CourseResponseDto> courseResponseDtos = new ArrayList<>();
        for (Course course : courseList) {
            courseResponseDtos.add(convertToCourseResponseDto(course));
        }
        return courseResponseDtos;
    }

    @Override
    public CourseResponseDto getCourseById(int id) {
        if (id <= 0) {
            throw new IdNotValidException("Given id " + id + " is not valid. Please enter valid id");
        }
        Course course = courseRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course with " + id + " doesn't exists"));

        return convertToCourseResponseDto(course);
    }

    @Override
    public void addNewCourse(CourseRequestDto courseRequestDto)  {
        Optional<Course> existingCourse = courseRepo.findByName(courseRequestDto.getName());

        if (existingCourse.isPresent()) {
            throw new CourseAlreadyExistsException("Submitted course already exists.");
        }

        if (courseRequestDto.getName() == null || courseRequestDto.getName().isEmpty()) {
            throw new InvalidDataException("Submitted data is invalid. Please enter valid data");
        }

        Course course = convertToCourse(courseRequestDto);


        courseRepo.save(course);
    }


}
