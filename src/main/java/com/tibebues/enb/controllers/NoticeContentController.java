package com.tibebues.enb.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.tibebues.enb.exceptions.ResourceNotFoundException;
import com.tibebues.enb.models.NoticeContent;
import com.tibebues.enb.repositories.NoticeContentRepository;
import com.tibebues.enb.repositories.NoticeRepository;

@RestController
public class NoticeContentController {
	
	@Autowired
	private NoticeContentRepository noticeContentRepository;
	@Autowired
	private NoticeRepository noticeRepository;
	
	@PostMapping("/notices/{noticeId}/notice-contents")
	public NoticeContent createNoticeContent(@PathVariable(value = "noticeId") long noticeId,@Valid @RequestBody NoticeContent noticeContent) throws ResourceNotFoundException {
		
		return noticeRepository.findById(noticeId).map(notice -> {
			noticeContent.setNotice(notice);
			return noticeContentRepository.save(noticeContent);
			}).orElseThrow( ()->new ResourceNotFoundException("notice not fund"));
	}

}
