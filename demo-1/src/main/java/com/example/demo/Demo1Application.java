package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
		System.out.println(runRegezx("Arhghgfghd@3294i93Test%$%^$^"));
	}

	public static boolean runRegezx(String input) {
		
		if(input.matches("^.*(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_0-9]).*$")) {
		return true;
		}
		return false;
	}
}
