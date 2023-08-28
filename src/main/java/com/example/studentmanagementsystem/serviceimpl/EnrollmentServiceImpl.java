package com.example.studentmanagementsystem.serviceimpl;

import com.example.studentmanagementsystem.exception.ResourceNotFoundException;
import com.example.studentmanagementsystem.model.Course;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.repo.CourseRepo;
import com.example.studentmanagementsystem.repo.StudentRepo;
import com.example.studentmanagementsystem.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    @Override
 @Transactional
    public void enrollment(int studentId, int courseId)  {
        Student existingStudent = studentRepo.getStudentByIdAndStatus(studentId).orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + " not found"));
        Course existingCourse = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course with id " + courseId + " not found"));

        existingStudent.getCourses().add(existingCourse);
        existingCourse.getStudents().add(existingStudent);

        //transaction ley method lai single task ko rup ma linxa
        //yo method ma 4 ota task xa if @Transactional rakhyana vanya transaction commit naii hudaina
        //so @Transactional halnaii paryo
    }
}
