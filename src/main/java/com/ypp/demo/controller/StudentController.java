package com.ypp.demo.controller;

import com.ypp.demo.model.Student;
import com.ypp.demo.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class StudentController {

    StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/list")
    List<Student> getStudents() {
        return  service.getStudents();
    }

    @GetMapping("/{id}")
    Student getStudent(@PathVariable int id) {
        return service.getStudent(id);
    }
}
