package com.tibebues.enb.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.tibebues.enb.exceptions.ResourceNotFoundException;
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
