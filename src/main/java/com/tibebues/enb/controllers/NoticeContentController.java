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
	

}
