package com.cico.service;

import java.io.IOException;

import com.cico.payload.QRResponse;
import com.google.zxing.WriterException;

public interface IQRService {

	public QRResponse generateQRCode() throws WriterException,IOException;
}
