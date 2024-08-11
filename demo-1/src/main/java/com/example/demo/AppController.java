package com.example.demo;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
	
	  @GetMapping("/ui")
	    public String greeting(Model model) {
	        model.addAttribute("message", "Welcome to our website!");
	        return "index"; // Thymeleaf template name (greeting.html)
	   }

}
