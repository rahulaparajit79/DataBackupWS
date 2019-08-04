package com.databackup.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.databackup.service.FileStorageService;

@RestController
@RequestMapping("/")
public class MainController {
	
	private final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Value( "${message.welcomeMessage}" )
	private String welcomeMsg;

	@GetMapping("/")
	public String welcome(){
		return welcomeMsg;
	}
	
	// 3.1.2 Multiple file upload
	@CrossOrigin(origins = "*", methods = {RequestMethod.POST,RequestMethod.GET})
    @PostMapping("/api/upload/multi")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles) {

        logger.debug("Multiple file upload!");
        
        System.out.println("ExtraField = "+ extraField);

        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

        	fileStorageService.saveUploadedFiles(Arrays.asList(uploadfiles),extraField);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);

    }
	
	@CrossOrigin(origins = "*", methods = {RequestMethod.POST,RequestMethod.GET})
    @GetMapping("/api/upload/listdir")
	public List<String> listBackupDir(){
		return fileStorageService.listBackupDirectory();
	}
	
	
}
