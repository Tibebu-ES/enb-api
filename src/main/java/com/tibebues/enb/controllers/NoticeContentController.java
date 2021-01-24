package com.tibebues.enb.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tibebues.enb.exceptions.ResourceNotFoundException;
import com.tibebues.enb.models.NoticeContent;
import com.tibebues.enb.payload.Response;
import com.tibebues.enb.repositories.NoticeContentRepository;
import com.tibebues.enb.repositories.NoticeRepository;
import com.tibebues.enb.service.FileStorageService;

@RestController
public class NoticeContentController {
	
	@Autowired
	private NoticeContentRepository noticeContentRepository;
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	
	
	@PostMapping("/notices/{noticeId}/notice-contents")
	public NoticeContent createNoticeContent(@PathVariable(value = "noticeId") long noticeId, 
			@RequestParam("imageFile") MultipartFile imageFile, 
			@RequestParam("type") String contentType
			) throws ResourceNotFoundException {
		
		//get the image file and upload to the upload directory, and store the file name to the database at the content_uri attri
		//store the file
		String fileName = fileStorageService.storeFile(imageFile);
		
		return noticeRepository.findById(noticeId).map(notice -> {
			NoticeContent noticeContent = new NoticeContent();
			noticeContent.setNotice(notice);
			noticeContent.setType(contentType);
			
			return noticeContentRepository.save(noticeContent);
			}).orElseThrow( ()->new ResourceNotFoundException("notice not fund"));
	}

}
