package com.cico.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cico.payload.QRResponse;
import com.cico.service.IQRService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class QRServiceImpl implements IQRService{

	@Override
	public QRResponse generateQRCode() throws WriterException, IOException {
		String randomData = UUID.randomUUID().toString();

        int imageSize = 283;
        BitMatrix matrix = new MultiFormatWriter().encode(randomData, BarcodeFormat.QR_CODE, imageSize, imageSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", bos);
        return new QRResponse(Base64.getEncoder().encodeToString(bos.toByteArray()), LocalDateTime.now());  
	}

}
