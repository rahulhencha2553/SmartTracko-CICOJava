package com.cico.service;

import java.util.List;

import com.cico.model.Fees;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;

public interface IFeesService {

	

	public Fees createStudentFees(Integer studentId, Integer courseId, Double finalFees, String date);

	public PageResponse<FeesResponse> feesList(Integer page, Integer size);

	public FeesResponse findByFeesId(Integer feesId);

	public List<FeesResponse> searchByName(String fullName);

	public List<FeesResponse> findFeesByDates(String startDate, String endDate);

	public PageResponse<FeesResponse> feesCompleteList(Integer page, Integer size);

}