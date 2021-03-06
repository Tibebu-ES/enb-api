package com.tibebues.enb.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
	
	/**
	 * Get the List of all notices
	 * @return
	 */
	@GetMapping("/notices")
	public List<Notice> getAllNotices() {
		return noticeRepository.findAll();
	}
	

	/**
	 * Get a specific notice by ID
	 * @param noticeId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/notices/{id}")
	public ResponseEntity<Notice> getNoticeById(@PathVariable(value = "id") long noticeId) throws ResourceNotFoundException{
		Notice notice  = noticeRepository.findById(noticeId).orElseThrow(() -> new ResourceNotFoundException("Notice not found with id="+noticeId));
		return ResponseEntity.ok(notice);
	}
	
	/**
	 * Get list of notices that need to be shown today.
	 * @return
	 * Each notice N such that 
	 * N.OpeningDate <= Today <= N.ClosingDate
	 */
	@GetMapping("/notices-today")
	public List<Notice> getTodayNotices(){
		return noticeRepository.findNoticesForToday(Calendar.getInstance().getTime());
	}
	
	/**
	 * Create a new notice. With out notice contents.
	 * @param notice
	 * @return
	 */
	@PostMapping("/notices")
	public Notice createNewNotice(@RequestBody Notice notice){
		return noticeRepository.save(notice);
	}
	
	
	/**
	 * Create a new Notice including notice contents of image type
	 * @param title
	 * @param message
	 * @param openingDate
	 * @param closingDate
	 * @param imageContents - array of images
	 * @return
	 * @throws Exception
	 */
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
	

	/**
	 * Update an existing notice
	 * @param id
	 * @param newNotice
	 * @return
	 * @throws ResourceNotFoundException
	 */
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
	
	/**
	 * Delete an existing notice
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@DeleteMapping("/notices/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteNotice(@PathVariable long id) throws ResourceNotFoundException{
		Notice notice = noticeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notice not found with id="+id));
		noticeRepository.delete(notice);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	
	
	
/*** ACCESS NOTICE CONTENTS*****************************************************/	

	/**
	 * get list of NoticeContents' ID of an existing  Notice 
	 * @param noticeId
	 * @return array of IDs of all notice contents associated with the noticeId
	 * 
	 */
	@GetMapping("/notice-contents/{noticeId}")
	public ResponseEntity<List<Long>> getAllNoticeContentIdOfNotice(@PathVariable long noticeId){
		List<NoticeContent> noticeContents = noticeContentRepository.findByNoticeId(noticeId);
		List<Long> response = new ArrayList<>();
		for (NoticeContent noticeContent : noticeContents) {
			response.add(noticeContent.getId());
		}
		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Get a specific Notice content of image type. Returns the image.
	 * @param id - Id of the notice content
	 * @return Image Resource
	 * return 404 response if content type is not image
	 * @throws ResourceNotFoundException
	 */
		@GetMapping("/notice-content-image/{id}")
	public ResponseEntity<Resource> getNoticeContentById(@PathVariable long id) throws ResourceNotFoundException{
		NoticeContent noticeContent = noticeContentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notice Content not found with id"+id));
		if(noticeContent.getType().contentEquals(NoticeContent.CONTENT_TYPE_VIDEO))
			throw new ResourceNotFoundException("Notice Content not image type with id"+id);
		return ResponseEntity.ok(fileStorageService.loadFileAsResource(noticeContent.getNoticeURL()));
	}
		
	
		/**
		 * Get a specific Notice content of video type. Returns the video url.
		 * @param id - Id of the notice content
		 * @return Video url 
		 * @throws ResourceNotFoundException
		 */
		@GetMapping("/notice-content-video/{id}")
	public ResponseEntity<String> getNoticeContentURLById(@PathVariable long id) throws ResourceNotFoundException{
		NoticeContent noticeContent = noticeContentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Notice Content not found with id"+id));
		if(noticeContent.getType().contentEquals(NoticeContent.CONTENT_TYPE_IMAGE))
			throw new ResourceNotFoundException("Notice Content not video type with id"+id);
		return ResponseEntity.ok(noticeContent.getNoticeURL());
	}		
/*** END --- ACCESS NOTICE CONTENTS  *****************************************************/			
	
/*** DELETE NOTICE CONTENT|S*****************************************************/	
	
		/**
		 * Delete all notice contents of a Notice 
		 * @param noticeId
		 * @return 
		 */
	@DeleteMapping("/notice-contents/{noticeId}")
	public ResponseEntity<Map<String, Boolean>> deleteNoticeContentsOfNotice(@PathVariable long noticeId){
			List<NoticeContent> noticeContents = noticeContentRepository.findByNoticeId(noticeId);
			noticeContentRepository.deleteInBatch(noticeContents);
			for (NoticeContent noticeContent : noticeContents) {
				//if content type is image, delete the image file from the filestorage
				if(noticeContent.getType().contentEquals(NoticeContent.CONTENT_TYPE_IMAGE))
					 fileStorageService.deleteFile(noticeContent.getNoticeURL());
			}
			Map<String, Boolean> response = new HashMap<>();
			response.put("deleted", true);
			return ResponseEntity.ok(response);
	}
	
	/**
	 * Delete a particular Notice Content of id
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@DeleteMapping("/notice-content/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteNoticeContentById(@PathVariable long id) throws ResourceNotFoundException{
			NoticeContent noticeContent = noticeContentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notice Content Not found with id="+id));
			noticeContentRepository.delete(noticeContent);
			//if content type is image, delete the image file from the filestorage
			if(noticeContent.getType().contentEquals(NoticeContent.CONTENT_TYPE_IMAGE))
				 fileStorageService.deleteFile(noticeContent.getNoticeURL());
			Map<String, Boolean> response = new HashMap<>();
			response.put("deleted", true);
			return ResponseEntity.ok(response);
	}
		
		
/*** END -  DELETE NOTICE CONTENT|S*****************************************************/	
	
/*** ADD  NOTICE CONTENT|S* *****************************************************/
	
	/**
	 * Add notice contents of type image to an existing notice
	 * @param noticeId
	 * @param imageContents array of image files
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PostMapping("/notice-contents-image")
	public List<NoticeContent> addNoticeContentsTypeImage(
			@RequestParam("noticeId") Long noticeId,
			@RequestParam("imageContents") MultipartFile[] imageContents ) throws ResourceNotFoundException
	{
		//get the notice
		Notice notice = noticeRepository.findById(noticeId).orElseThrow(()-> new ResourceNotFoundException("Notice not found with id"+noticeId));
		
		List<NoticeContent> noticeContents = new ArrayList<>();
		for (MultipartFile imageContent : imageContents) {
			String fileName = fileStorageService.storeFile(imageContent);
			NoticeContent noticeContent = new NoticeContent();
			noticeContent.setType(NoticeContent.CONTENT_TYPE_IMAGE);
			noticeContent.setNoticeURL(fileName);
			noticeContent.setNotice(notice);
			noticeContents.add(noticeContent);
		}
		
		return noticeContentRepository.saveAll(noticeContents);
		
	}
	
	
	
	/**
	 * Add notice contents of type video to an existing  notice
	 * @param noticeId
	 * @param videoURLs
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PostMapping("/notice-contents-video")
	public List<NoticeContent> addNoticeContentsTypeVideo(
			@RequestParam("noticeId") Long noticeId,
			@RequestParam("videoURLs") String[] videoURLs ) throws ResourceNotFoundException
	{
		//get the notice
		Notice notice = noticeRepository.findById(noticeId).orElseThrow(()-> new ResourceNotFoundException("Notice not found with id"+noticeId));
		
		List<NoticeContent> noticeContents = new ArrayList<>();
		for (String videoURL : videoURLs) {
			NoticeContent noticeContent = new NoticeContent();
			noticeContent.setType(NoticeContent.CONTENT_TYPE_VIDEO);
			noticeContent.setNoticeURL(videoURL);
			noticeContent.setNotice(notice);
			noticeContents.add(noticeContent);
		}
		
		return noticeContentRepository.saveAll(noticeContents);
		
	}
	
	
/*** END  NOTICE CONTENT|S*****************************************************/	
	
}

