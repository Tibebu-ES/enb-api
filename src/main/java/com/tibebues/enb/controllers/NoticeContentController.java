package com.tibebues.enb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.tibebues.enb.repositories.NoticeContentRepository;
import com.tibebues.enb.repositories.NoticeRepository;

@RestController
public class NoticeContentController {
	
	@Autowired
	private NoticeContentRepository noticeContentRepository;
	@Autowired
	private NoticeRepository noticeRepository;
	
	

}
