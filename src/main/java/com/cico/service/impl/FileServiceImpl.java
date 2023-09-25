package com.cico.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
	

//	public String uploadFileInFolder(MultipartFile imageFile, String destinationPath) {
//		String currentDir = System.getProperty("user.dir")+destinationPath;
//	    String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
//	    String randomId = UUID.randomUUID().toString();
//		String randomName =  randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
//	    String imagePath = currentDir + randomName;
//	    try {
//	        File destinationFile = new File(imagePath);
//	        FileUtils.forceMkdirParent(destinationFile);
//	        imageFile.transferTo(destinationFile);
//	        return randomName;
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//	}
	
	public String uploadFileInFolder(MultipartFile imageFile, String destinationPath) {
		 int targetFileSizeKB=1;  
		String currentDir = System.getProperty("user.dir") + destinationPath;
	        String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
	        String randomId = UUID.randomUUID().toString();
	        String randomName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
	        String imagePath = currentDir + randomName;

	        try {
	            File destinationFile = new File(imagePath);
	            imageFile.transferTo(destinationFile);

	            // Load the uploaded image as BufferedImage
	            BufferedImage originalImage = ImageIO.read(destinationFile);

	            // Iterate to find the compression quality that meets the target file size
	            int compressionQuality = 95; // Start with a high quality
	            BufferedImage compressedImage = originalImage;

	            while (true) {
	                // Save the compressed image to a temporary file
	                File tempFile = new File(imagePath + ".temp");
	                ImageIO.write(compressedImage, "jpg", tempFile);

	                // Check the size of the temporary file
	                long fileSizeKB = tempFile.length() / 1024;

	                // Delete the temporary file
	                tempFile.delete();

	                // Break the loop if the size is within the target limit or if quality is too low
	                if (fileSizeKB <= targetFileSizeKB || compressionQuality <= 5) {
	                    break;
	                }

	                // Reduce the compression quality and iterate again
	                compressionQuality -= 5;
	                compressedImage = compressImage(originalImage, compressionQuality);
	            }

	            // Save the final compressed image back to the destination
	            ImageIO.write(compressedImage, "jpg", destinationFile);

	            return randomName;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	private BufferedImage compressImage(BufferedImage originalImage, float compressionQuality) {
        // Create a new buffered image with the same dimensions and type as the original
        BufferedImage compressedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                originalImage.getType());

        // Perform JPEG compression to a ByteArrayOutputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Write the compressed image to the ByteArrayOutputStream
            ImageIO.write(originalImage, "jpg", baos);

            // Get the compressed image data
            byte[] compressedImageData = baos.toByteArray();

            // Read the compressed data into the new buffered image
            compressedImage = ImageIO.read(new ByteArrayInputStream(compressedImageData));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedImage;
    }    
	
	@Override
	public InputStream getImages(String fileName,String destination){
		//System.out.println(System.getProperty("user.dir")+globalImagesPath+destination+File.separator+fileName);
		try {
				return new FileInputStream(System.getProperty("user.dir")+globalImagesPath+destination+File.separator+fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	 public Resource loadFileAsResource(String filename,String destination) {
	        try {
	            Path filePath = Paths.get(System.getProperty("user.dir") +globalImagesPath+ destination).resolve(filename).normalize();
	            System.out.println(filePath);
	            Resource resource = new UrlResource(filePath.toUri());

	            if (resource.exists()) {
	                return resource;
	            } else {
	                throw new RuntimeException("File not found: " + filename);
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Could not load file: " + filename, e);
	        }
	    }

	
	@Override
	public InputStream getAttachment(String attachment){
		//System.out.println(System.getProperty("user.dir")+workReportUploadPath+attachment);
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
	
	public String uplaoadFile(MultipartFile file,String destinationPath) {
		String currentDir = System.getProperty("user.dir") + destinationPath;
		System.out.println(currentDir);
		String name = StringUtils.cleanPath(file.getOriginalFilename());
		 Path path = Paths.get(currentDir , name );
		 try {
			 Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
		 }catch(Exception e) {
			 
		 }
		 return name;
	}
}
