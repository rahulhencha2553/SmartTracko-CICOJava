package com.cico.service;

import com.cico.model.FeesPay;
import com.cico.payload.FeesPayResponse;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;

public interface IFeesPayService {

	public FeesPay feesPayService(Integer feesId, Double feesPayAmount, String payDate, String recieptNo, String description);

	public PageResponse<FeesResponse> feesPayList(Integer page, Integer size);

}
