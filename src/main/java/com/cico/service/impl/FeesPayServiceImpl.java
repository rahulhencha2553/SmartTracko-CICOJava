package com.cico.service.impl;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Fees;
import com.cico.model.FeesPay;
import com.cico.payload.FeesPayResponse;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;
import com.cico.repository.FeesPayRepository;
import com.cico.repository.FeesRepository;
import com.cico.service.IFeesPayService;

@Service
public class FeesPayServiceImpl implements IFeesPayService {

	@Autowired
	private FeesPayRepository feesPayRepository;
	@Autowired
	private FeesRepository feesRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public FeesPay feesPayService(Integer feesId, Double feesPayAmount, String payDate, String recieptNo,
			String description) {

		FeesPay feesPay = new FeesPay(feesPayAmount, LocalDate.parse(payDate), recieptNo, description);
		Fees findByFeesId = feesRepository.findByFeesId(feesId);

		if (findByFeesId.getRemainingFees() != 0) {
			feesPay.setFees(feesRepository.findById(feesId).get());
			feesPay.setFeesPayAmount(feesPayAmount);
			Fees fees = feesPay.getFees();
			if (feesPay.getFeesPayAmount() <= findByFeesId.getRemainingFees()) {
				fees.setRemainingFees(fees.getRemainingFees() - feesPay.getFeesPayAmount());
				fees.setFeesPaid(fees.getFinalFees() - fees.getRemainingFees());

				if (findByFeesId.getRemainingFees() == 0) {
					feesRepository.updateIsCompleted(feesId);
				}
				feesRepository.save(fees);

				return feesPayRepository.save(feesPay);
			} else
				throw new ResourceNotFoundException("Fees Amount Is Greate then Remaining Fees Amount");
		}
		throw new ResourceNotFoundException("Fees Not Found");

	}

	@Override
	public PageResponse<FeesResponse> feesPendingList(Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "feesId");
		Page<Fees> fees = feesRepository.findByRemainingFees(pageable);

		if (fees.getNumberOfElements() == 0) {
			return new PageResponse<>(Collections.emptyList(), fees.getNumber(), fees.getSize(),
					fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
		}
		List<FeesResponse> asList = Arrays.asList(mapper.map(fees.getContent(), FeesResponse[].class));

		return new PageResponse<>(asList, fees.getNumber(), fees.getSize(), fees.getTotalElements(),
				fees.getTotalPages(), fees.isLast());
	}

	@Override
	public ResponseEntity<?> getAllTransectionByStudentId(Integer studentId) {
		Fees fees = feesRepository.findFeesByStudentId(studentId);
		List<FeesPayResponse> payResponse = new ArrayList<>();
		List<FeesPay> findByFees = feesPayRepository.findByFees(fees);

		for (FeesPay feesPay : findByFees) {
			payResponse.add(mapper.map(feesPay, FeesPayResponse.class));
		}

		return new ResponseEntity<>(payResponse, HttpStatus.OK);
	}

	@Override
	public PageResponse<FeesPayResponse> feesPayList(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "payId");
		Page<FeesPay> fees = feesPayRepository.findByFeesPayAmount(pageable);

		if (fees.getNumberOfElements() == 0) {
			return new PageResponse<>(Collections.emptyList(), fees.getNumber(), fees.getSize(),
					fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
		}
		List<FeesPayResponse> asList = Arrays.asList(mapper.map(fees.getContent(), FeesPayResponse[].class));

		return new PageResponse<>(asList, fees.getNumber(), fees.getSize(), fees.getTotalElements(),
				fees.getTotalPages(), fees.isLast());
	}

	@Override
	public FeesPayResponse findByPayId(Integer payId) {
		// TODO Auto-generated method stub
		FeesPay feesPay = feesPayRepository.findById(payId)
				.orElseThrow(() -> new ResourceNotFoundException("Fees Pay not found from given Id"));
		return mapper.map(feesPay, FeesPayResponse.class);
	}

	@Override
	public FeesPay updateFeesPay(FeesPay feesPay) {
		FeesPay feesPayData = feesPayRepository.findByPayId(feesPay.getPayId());
		System.out.println(feesPayData);
		if (Objects.nonNull(feesPayData)) {

			if (feesPay.getPayDate() != null) {
				feesPayData.setPayDate(feesPay.getPayDate());
			} else {
				feesPay.setPayDate(feesPay.getPayDate());
			}
			Fees fees = feesPayData.getFees();
			fees.setFeesPaid(fees.getFeesPaid() - feesPayData.getFeesPayAmount());
			fees.setFeesPaid(fees.getFeesPaid() + feesPay.getFeesPayAmount());
			feesPayData.setFeesPayAmount(feesPay.getFeesPayAmount());
			fees.setRemainingFees(fees.getFinalFees() - fees.getFeesPaid());
			if (fees.getFeesPaid() <= fees.getFinalFees()) {
				if (fees.getRemainingFees() == 0) {
					feesRepository.updateIsCompleted(fees.getFeesId());
				} else {
					feesRepository.updateNotIsCompleted(fees.getFeesId());
				}

				feesPayData.setFees(fees);
				feesRepository.save(fees);
				return feesPayRepository.save(feesPayData);
			}
		} else {
			throw new ResourceNotFoundException("Fees Amount Is Greate then Remaining Fees Amount");
		}
		throw new ResourceNotFoundException("Fees Pay Not Found");
	}

	@Override
	public List<FeesPayResponse> searchByNameInFeesPayList(String fullName) {
		List<FeesPay> findByFullName = feesPayRepository.findByFullName(fullName);
		List<FeesPayResponse> asList = Arrays.asList(mapper.map(findByFullName, FeesPayResponse[].class));
		return asList;
	}

	@Override
	public List<FeesPayResponse> searchByMonthInFeesPayList(String startDate, String endDate) {
		List<FeesPay> findByMonth = feesPayRepository.findByMonth(LocalDate.parse(startDate),LocalDate.parse(endDate));
		List<FeesPayResponse> asList = Arrays.asList(mapper.map(findByMonth, FeesPayResponse[].class));
		return asList;
	}

}
