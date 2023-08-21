package com.cico.service;

import org.springframework.http.ResponseEntity;

import com.cico.model.FeesPay;
import com.cico.payload.FeesPayResponse;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;

public interface IFeesPayService {

	public FeesPay feesPayService(Integer feesId, Double feesPayAmount, String payDate, String recieptNo, String description);

	public PageResponse<FeesResponse> feesPayList(Integer page, Integer size);

	public ResponseEntity<?> getAllTransectionByStudentId(Integer studentId);

}
