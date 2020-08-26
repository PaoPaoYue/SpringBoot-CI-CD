package com.ypp.demo;

import com.ypp.demo.config.AppConfiguration;
import com.ypp.demo.mapper.StudentMapper;
import com.ypp.demo.model.Student;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

@SpringBootTest
class DemoApplicationTests {

	Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	ApplicationContext ioc;

	@Autowired
	AppConfiguration config;

	@Autowired
	StudentMapper mapper;

	@Test
	void contextLoads() {

		Student student = mapper.getStudent(0);
		logger.info(student.toString());
		logger.info("success");
	}

}
