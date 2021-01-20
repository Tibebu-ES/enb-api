package com.tibebues.enb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {
	
	//get All notices
	@GetMapping("/notices")
	public String getAllNotices() {
		return "Hello! From get Notice Controller";
	}

}
