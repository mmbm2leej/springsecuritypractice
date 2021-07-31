package com.example.demo.student;

import com.example.demo.student.models.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/management/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jerry Thompson"),
            new Student(2,"Cole Stevens"),
            new Student(3,"Joan Sampson"),
            new Student(4,"Mira Jones")
    );

    //hasRole("ROLE_") hasAnyRole("ROLE_")
    //hasAuthority("permission") hasAnyAuthority("permission")

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<Student> getAllStudents() {
        System.out.println("getAllStudents GET");
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println("registerNewStudent POST");
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentID}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentID") Integer studentID) {
        System.out.println("deleteStudent DELETE");
        System.out.println(studentID);
    }

    @PutMapping(path = "{studentID}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentID") Integer studentID,
                              @RequestBody Student student) {
        System.out.println("updateStudent PUT");
        System.out.println(String.format("%s %s", studentID, student));
    }

}
