package com.cico.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.cico.config.Qrresponse;
import com.cico.config.SocketHandler;
import com.cico.model.Attendance;
import com.cico.model.QrManage;
import com.cico.model.Student;
import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.payload.QRResponse;
import com.cico.repository.AttendenceRepository;
import com.cico.repository.QrManageRepository;
import com.cico.repository.StudentRepository;
import com.cico.security.JwtUtil;
import com.cico.service.IQRService;
import com.cico.util.AppConstants;
import com.cico.util.Roles;
import com.google.gson.Gson;
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

	@Value("${cico.key}")
	private String qrSecretKey;

	@Autowired
	private AttendenceRepository attendenceRepository;

	@Override
	public QRResponse generateQRCode() throws WriterException, IOException {
		String randomData = qrSecretKey + UUID.randomUUID().toString();

		int imageSize = 283;
		BitMatrix matrix = new MultiFormatWriter().encode(randomData, BarcodeFormat.QR_CODE, imageSize, imageSize);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "png", bos);
		return new QRResponse(Base64.getEncoder().encodeToString(bos.toByteArray()), randomData, LocalDateTime.now());
	}

	@Override
	public ResponseEntity<?> QRLogin(String qrKey, String token) {
		qrKey = "CICO#" + qrKey;
		String[] split = qrKey.split("#");

		if (split[0].equals("CICO")) {
			QrManage findByUuid = qrManageRepository.findByUuid(split[1]);
			if (Objects.isNull(findByUuid)) {
				String username = util.getUsername(token);
				if (Objects.isNull(qrManageRepository.findByUserId(username))) {
					findByUuid = new QrManage(username, split[1]);
					qrManageRepository.save(findByUuid);
				}
				JwtResponse message = ClientLogin(token);
				message.setToken(token);
				jobEnd(split[1], message.getToken());
				return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, AppConstants.SUCCESS, HttpStatus.OK),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
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

//	public void jobEnd(String qrKey, String message) {
//
//		List<WebSocketSession> sessions = SocketHandler.qrSessions;
//		sessions.forEach(obj -> {
//			Map<String, Object> attributes = obj.getAttributes();
//			System.out.println("session key are"+ attributes.get("sessionId"));
//			if (attributes.get("sessionId").equals(qrKey)) {
//				try {
//					System.out.println("sending to session id ---> "+attributes.get("sessionId"));
//					obj.sendMessage(new TextMessage(new Gson().toJson(message)));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public void jobEnd(String qrKey, String message) {
		List<WebSocketSession> sessions = SocketHandler.qrSessions;
		for (WebSocketSession session : sessions) {
			Map<String, Object> attributes = session.getAttributes();
			Object sessionId = attributes.get("sessionId");
			if (sessionId != null && sessionId.equals(qrKey)) {
				try {
					//Qrresponse res = new Qrresponse();
					//res.token = message;
					//res.type = "QrResponse";
					session.sendMessage(new TextMessage(new Gson().toJson(message)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ResponseEntity<?> getLinkedDeviceData(HttpHeaders headers) {
		String username = util.getUsername(headers.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(headers.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDate(studentId, LocalDate.now());
		QrManage findByUserId = qrManageRepository.findByUserId(username);
		Map<String, Object> response = new HashMap<>();
		response.put("loginDevice", findByUserId);
		response.put("attendance", attendance);
		response.put("loginAt", findByUserId != null ? findByUserId.getLoginAt().toString().replace('T', ' ') : "");
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<?> removeDeviceFromWeb(HttpHeaders headers) {
		String username = util.getUsername(headers.getFirst(AppConstants.AUTHORIZATION));
		QrManage findByUserId = qrManageRepository.findByUserId(username);
		System.out.println(username);
		System.err.println("qr " + findByUserId);
		if (findByUserId != null) {
			System.err.println(findByUserId);
			jobEnd(findByUserId.getUuid(), "LOGOUT");
			qrManageRepository.delete(findByUserId);
			return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, AppConstants.SUCCESS, HttpStatus.OK),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> updateWebLoginStatus(String token, String os, String deviceType, String browser) {
		String username = util.getUsername(token);
		System.out.println("in web update " + token);
		QrManage findByUserId = qrManageRepository.findByUserId(username);
		findByUserId.setBrowser(browser);
		findByUserId.setDeviceType(deviceType);
		findByUserId.setOs(os);
		findByUserId.setLoginAt(LocalDateTime.now());
		QrManage save = qrManageRepository.save(findByUserId);
		return new ResponseEntity<>(save, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getLinkedDeviceDataByUuid(String key) {
		QrManage findByUuid = qrManageRepository.findByUuid(key);
		Map<String, Object> response = new HashMap<>();
		response.put("loginDevice", findByUuid);
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
