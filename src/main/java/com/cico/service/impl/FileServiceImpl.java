package com.cico.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cico.service.IFileService;


@Service
public class FileServiceImpl implements IFileService{

	@Value("${jobAlertImages}")
	private  String fileUploadPath ;
	
	@Value("${workReportUploadPath}")
	private String workReportUploadPath;
	
	@Value("${technologyStackImages}")
	private String technologyStackImagePath;
	
	@Value("${globalImagesPath}")
	private String globalImagesPath;
	

	public String uploadFileInFolder(MultipartFile imageFile, String destinationPath) {
		String currentDir = System.getProperty("user.dir")+destinationPath;
	    String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
	    String randomId = UUID.randomUUID().toString();
		String randomName =  randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
	    String imagePath = currentDir + randomName;
	    try {
	        File destinationFile = new File(imagePath);
	        FileUtils.forceMkdirParent(destinationFile);
	        imageFile.transferTo(destinationFile);
	        return randomName;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	@Override
	public InputStream getImages(String fileName,String destination){
		System.out.println(System.getProperty("user.dir")+globalImagesPath+destination+File.separator+fileName);
		try {
			return new FileInputStream(System.getProperty("user.dir")+globalImagesPath+destination+File.separator+fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	
	@Override
	public InputStream getAttachment(String attachment){
		System.out.println(System.getProperty("user.dir")+workReportUploadPath+attachment);
		try {
			return new FileInputStream(System.getProperty("user.dir")+workReportUploadPath + File.separator +attachment);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void deleteImagesInFolder(List<String> images,String path) {
		
		for(String name : images) {
			Path fileToDeletePath = Paths.get(path+name);
			try {
				Files.delete(fileToDeletePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
