package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	/**
	 * Just for initial loading
	 */
	@RequestMapping("/")
	public String home() {
		return "index";
	}
}
