package com.ypp.demo.service;

import com.ypp.demo.mapper.StudentMapper;
import com.ypp.demo.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    StudentMapper mapper;

    public StudentService(StudentMapper mapper) {
        this.mapper = mapper;
    }

    public List<Student> getStudents() {
        return  mapper.getStudents();
    }

    public Student getStudent(int id) {
        return mapper.getStudent(id);
    }
}
