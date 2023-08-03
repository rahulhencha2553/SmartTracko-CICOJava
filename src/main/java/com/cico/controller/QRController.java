package com.cico.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.payload.QRResponse;
import com.cico.security.JwtUtil;
import com.cico.service.IQRService;

@RestController
@RequestMapping("/qr")
@CrossOrigin("*")
public class QRController {

	@Autowired
	private IQRService qrService;
	
	@Autowired
	private JwtUtil util;
	
	@Autowired
	private IQRService service;
	
	
	@GetMapping("/qrGenerator")
	public ResponseEntity<QRResponse> generateQrCodeAsBase64() throws Exception{
		 QRResponse generateQRCode = qrService.generateQRCode();
      return ResponseEntity.ok(generateQRCode);
	}
	
	@PostMapping("/qrlogin/{qrKey}/{token}")
    public ResponseEntity<?> qrLoginWithToken(@PathVariable("qrKey") String qrKey,@PathVariable("token") String token){
        if(Objects.isNull(qrKey)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ApiResponse response = qrService.QRLogin(qrKey, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/clientlogin")
    public ResponseEntity<?> qrLoginClientAuthentication(@RequestParam("token") String token){
    	JwtResponse clientLogin = qrService.ClientLogin(token);
    	System.out.println("response responseresponseresponseresponseresponse "+clientLogin);
        return new ResponseEntity<>(clientLogin,HttpStatus.OK);
    }

}
