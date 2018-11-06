package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.engine.spi.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import controller.LoginController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class, PersistenceContext.class})
@SpringBootTest(classes = com.example.demo.DemoApplication.class)
public class SmokeTest {

	@Autowired
	private LoginController controller;

    @Test
    public void contexLoads() throws Exception {
    	
        assertThat(controller).isNotNull();
    }

}
