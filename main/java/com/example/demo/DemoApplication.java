package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import config.JwtAdminFilter;
import config.JwtFilter;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"controller", "service"})
@EnableJpaRepositories("model")
@EntityScan("model")
public class DemoApplication {
	
	// Filter for normal users, which are authorized to access /lights/*
	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/lights/*");

		return registrationBean;
	}
	
	// Filter for administrators. 
	@Bean
	public FilterRegistrationBean<JwtAdminFilter> jwtFilterAdmin() {
		final FilterRegistrationBean<JwtAdminFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtAdminFilter());
		registrationBean.addUrlPatterns("/admin/*");

		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
