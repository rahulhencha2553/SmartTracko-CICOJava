package com.cico.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.cico.model.QrManage;
import com.cico.model.Student;
import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.payload.QRResponse;
import com.cico.repository.QrManageRepository;
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
public class QRServiceImpl implements IQRService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private QrManageRepository qrManageRepository;

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
		return new QRResponse(Base64.getEncoder().encodeToString(bos.toByteArray()), randomData, LocalDateTime.now());
	}

	@Override
	public ResponseEntity<?>  QRLogin(String qrKey, String token) {
		QrManage findByUuid = qrManageRepository.findByUuid(qrKey);
		System.out.println("***************** "+findByUuid+" *******************");
		if(Objects.isNull(findByUuid)) {
			
			String username = util.getUsername(token);
			findByUuid = new QrManage(username,qrKey);
			QrManage qrManage = qrManageRepository.save(findByUuid);
			JwtResponse message = ClientLogin(token);
			executor.submit(() -> {
				message.setToken(token);
				jobEnd(qrKey, message);
			});
				return new ResponseEntity<> (new ApiResponse(Boolean.TRUE, AppConstants.SUCCESS, HttpStatus.OK),HttpStatus.OK);
		}
		else
			return new ResponseEntity<> (new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
	}

	
	public JwtResponse ClientLogin(String token) {
		String username = util.getUsername(token);
		Student student = studentRepository.findByUserId(username);
		String newToken = util.generateTokenForStudent(student.getStudentId().toString(), student.getUserId(),
				student.getDeviceId(), Roles.STUDENT.toString());
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(newToken);
		return jwtResponse;
	}

	private void jobEnd(String qrKey, JwtResponse message) {
		messageSendingOperations.convertAndSend("/queue/messages-" + qrKey, message.getToken());
	}

	
	public ResponseEntity<?> getLinkedDeviceData(HttpHeaders headers) {
		String username = util.getUsername(headers.getFirst(AppConstants.AUTHORIZATION));
		QrManage findByUserId = qrManageRepository.findByUserId(username);
		Map<String, Object> response = new HashMap<>();
		response.put("loginDevice", findByUserId);
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	
	public ResponseEntity<?> removeDeviceFromWeb(HttpHeaders headers) {
		String username = util.getUsername(headers.getFirst(AppConstants.AUTHORIZATION));
		int check = qrManageRepository.deleteByUserId(username);
		if(check != 0)
			return new ResponseEntity<>(new ApiResponse(Boolean.TRUE,AppConstants.SUCCESS, HttpStatus.OK),HttpStatus.OK);
		return new ResponseEntity<>(new ApiResponse(Boolean.FALSE,AppConstants.FAILED, HttpStatus.OK),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateWebLoginStatus(String token, String os, String deviceType, String browser) {
		String username = util.getUsername(token);
		QrManage findByUserId = qrManageRepository.findByUserId(username);
			findByUserId.setBrowser(browser);
			findByUserId.setDeviceType(deviceType);
			findByUserId.setOs(os);
			findByUserId.setLoginAt(LocalDateTime.now());
			QrManage save = qrManageRepository.save(findByUserId);
			return new ResponseEntity<>(save,HttpStatus.OK);
	}
	
	

}

