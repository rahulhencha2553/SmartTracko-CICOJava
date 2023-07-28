package com.cico.service;

import java.io.IOException;

import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.payload.QRResponse;
import com.google.zxing.WriterException;

public interface IQRService {

	public QRResponse generateQRCode() throws WriterException,IOException;
	
	public ApiResponse QRLogin(String qrKey,String token);
	
	public JwtResponse ClientLogin(String token);
}
