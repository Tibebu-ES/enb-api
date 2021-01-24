package com.tibebues.enb.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tibebues.enb.exceptions.ResourceNotFoundException;
import com.tibebues.enb.models.Notice;
import com.tibebues.enb.models.NoticeContent;
import com.tibebues.enb.repositories.NoticeContentRepository;
import com.tibebues.enb.repositories.NoticeRepository;
import com.tibebues.enb.service.FileStorageService;

@RestController
public class NoticeController {
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private NoticeContentRepository noticeContentRepository;

	@Autowired
	private FileStorageService fileStorageService;
	
	//get All notices 
	@GetMapping("/notices")
	public List<Notice> getAllNotices() {
		return noticeRepository.findAll();
	}
	
	//get notice by id
	@GetMapping("/notices/{id}")
	public ResponseEntity<Notice> getNoticeById(@PathVariable(value = "id") long noticeId) throws ResourceNotFoundException{
		Notice notice  = noticeRepository.findById(noticeId).orElseThrow(() -> new ResourceNotFoundException("Notice not found with id="+noticeId));
		return ResponseEntity.ok(notice);
	}
	
	//get notice by opening date and closing date - tobe done
	
	//create new Notice
	@PostMapping("/notices")
	public Notice createNewNotice(@RequestBody Notice notice){
		return noticeRepository.save(notice);
	}
	
	//create a new notice --including notice contents of type image
	
	@PostMapping("/notices-with-contents")
	public Notice createNewNoticeWithImageContents(
			@RequestParam("title") String title,
			@RequestParam("message") String message,
			@RequestParam("openingDate") String openingDate,
			@RequestParam("closingDate") String closingDate,
			@RequestParam("imageContents") MultipartFile[]  imageContents) throws Exception{
		//create Notice object
		Notice notice = new Notice();
		//set notice values
		notice.setTitle(title);
		notice.setMessage(message);
		try {
			notice.setOpeningDate(new SimpleDateFormat("dd/MM/yyyy").parse(openingDate));
			notice.setClosingDate(new SimpleDateFormat("dd/MM/yyyy").parse(closingDate));
			notice.setCreatedDate(Calendar.getInstance().getTime());
		} catch (ParseException e) {
			throw new ParseException(e.getMessage(), e.getErrorOffset());
		}
		//save notice
		Notice insertedNotice = noticeRepository.save(notice);
		
		//for each attached imageContent: store the Image to the upload dir --- prepare Notice Content and insert to the NoticeContent table
		for (MultipartFile imageContent : imageContents) {
			String fileName = fileStorageService.storeFile(imageContent);
			NoticeContent noticeContent = new NoticeContent();
			noticeContent.setType("image");
			noticeContent.setNoticeURL(fileName);
			noticeContent.setNotice(insertedNotice);
			//insert to the database
			noticeContentRepository.save(noticeContent);
		}
		return insertedNotice;
	}
	
	//update a notice
	@PutMapping("/notices/{id}")
	public ResponseEntity<Notice> updateNotice(@PathVariable(value = "id")long id, @RequestBody Notice newNotice) throws ResourceNotFoundException{
		Notice oldNotice = noticeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notice not found with id="+id));
		oldNotice.setOpeningDate(newNotice.getOpeningDate());
		oldNotice.setClosingDate(newNotice.getClosingDate());
		oldNotice.setCreatedDate(newNotice.getCreatedDate());
		oldNotice.setMessage(newNotice.getMessage());
		oldNotice.setTitle(newNotice.getTitle());
		
		Notice updatedNotice = noticeRepository.save(oldNotice);
		return ResponseEntity.ok(updatedNotice);
	}
	
	//delete Notice
	@DeleteMapping("/notices/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteNotice(@PathVariable long id) throws ResourceNotFoundException{
		Notice notice = noticeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notice not found with id="+id));
		noticeRepository.delete(notice);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	

}
