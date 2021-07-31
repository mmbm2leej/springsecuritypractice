package com.example.demo.student;

import com.example.demo.student.models.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jerry Thompson"),
            new Student(2,"Cole Stevens"),
            new Student(3,"Joan Sampson"),
            new Student(4,"Mira Jones")
    );



    @GetMapping(path="/all")
    public List<Student> getAllStudents() {
        return STUDENTS;
    }

    @GetMapping(path="/{studentID}")

    public Student getStudent(@PathVariable("studentID") Integer studentID) {

        return STUDENTS.stream().
                filter(student -> studentID.equals(student.getStudentID()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                ("Student " + studentID + "does not exist!")
                        ));

    }


}