package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.QRResponse;
import com.cico.service.IQRService;

@RestController
@RequestMapping("/qr")
@CrossOrigin("*")
public class QRController {

	@Autowired
	private IQRService qrService;
	
	@GetMapping("/qrGenerator")
	public ResponseEntity<QRResponse> generateQrCodeAsBase64() throws Exception{
		 QRResponse generateQRCode = qrService.generateQRCode();
      return ResponseEntity.ok(generateQRCode);
	}
	
	@PostMapping("/qrMatcher")
	public ResponseEntity<?> qrMatcher(){
		return ResponseEntity.ok("");
	}	
}
