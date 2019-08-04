package com.databackup.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	
	@Value( "${file.upload-dir}" )
	private String UPLOADED_FOLDER;
	
	//save file
    public void saveUploadedFiles(List<MultipartFile> files, String directory) throws IOException {

    	File dir = new File(UPLOADED_FOLDER+directory);
        if(!dir.exists()) {
        	dir.mkdir();
        }
        System.out.println(dir.getAbsolutePath());
        
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }
            
            

            byte[] bytes = file.getBytes();
            Path path = Paths.get(dir.getAbsolutePath()+File.separator + file.getOriginalFilename());
            Files.write(path, bytes);

        }

    }
    
    public List<String> listBackupDirectory(){
    	
    	File[] dirs = new File(UPLOADED_FOLDER).listFiles();
    	List<String> dirList = new ArrayList<>();
    	
    	for(File f:dirs) {
    		if(f.isDirectory()) {
    		dirList.add(f.getName());
    		}
    	}
    	
    	return dirList;
    }

}
