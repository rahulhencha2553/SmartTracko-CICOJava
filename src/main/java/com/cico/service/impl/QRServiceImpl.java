package com.cico.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.cico.model.Student;
import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.payload.QRResponse;
import com.cico.repository.StudentRepository;
import com.cico.security.JwtUtil;
import com.cico.service.IQRService;
import com.cico.util.AppConstants;
import com.cico.util.Roles;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class QRServiceImpl implements IQRService{

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JwtUtil util;
	
	@Autowired
    private SimpMessageSendingOperations messageSendingOperations;
	
	ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@Override
	public QRResponse generateQRCode() throws WriterException, IOException {
		String randomData = UUID.randomUUID().toString();

        int imageSize = 283;
        BitMatrix matrix = new MultiFormatWriter().encode(randomData, BarcodeFormat.QR_CODE, imageSize, imageSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", bos);
        return new QRResponse(Base64.getEncoder().encodeToString(bos.toByteArray()),randomData,LocalDateTime.now());  
	}

	@Override
	public ApiResponse QRLogin(String qrKey,String token) {
	
         JwtResponse message = new JwtResponse();
        executor.submit(()->{
            message.setToken(token);
            jobEnd(qrKey, message);
        });
        if(token.equals(token))
			return new ApiResponse(Boolean.TRUE,AppConstants.SUCCESS, HttpStatus.OK);
        else
        	return new ApiResponse(Boolean.FALSE, AppConstants.FAILED,HttpStatus.BAD_REQUEST);
	}

	@Override
	public JwtResponse ClientLogin(String token) {
		String username = util.getUsername(token);
		Student student = studentRepository.findByUserId(username);
		String newToken = util.generateTokenForStudent(student.getStudentId().toString(), student.getUserId(), student.getDeviceId(), Roles.STUDENT.toString());
		return new JwtResponse(newToken);
	}
	
	 private void jobEnd(String qrKey, JwtResponse message){
	        messageSendingOperations.convertAndSend(
	                "/queue/messages-"+qrKey, message.getToken());
	 }	

}
