package com.ypp.demo.mapper;

import com.ypp.demo.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentMapper {

    @Select("SELECT * FROM student")
    List<Student> getStudents();

    @Select("SELECT * FROM student WHERE id = #{id}")
    Student getStudent(@Param("id") int id);
}
