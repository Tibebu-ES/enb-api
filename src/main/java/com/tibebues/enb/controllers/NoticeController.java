package com.tibebues.enb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tibebues.enb.models.Notice;
import com.tibebues.enb.repositories.NoticeRepository;

@RestController
public class NoticeController {
	@Autowired
	private NoticeRepository noticeRepository;
	
	//get All notices
	@GetMapping("/notices")
	public List<Notice> getAllNotices() {
		return noticeRepository.findAll();
	}

}
