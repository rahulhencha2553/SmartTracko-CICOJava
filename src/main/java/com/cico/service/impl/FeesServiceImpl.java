package com.cico.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Fees;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;
import com.cico.repository.CourseRepository;
import com.cico.repository.FeesRepository;
import com.cico.repository.StudentRepository;
import com.cico.service.IFeesService;
import com.cico.util.AppConstants;

@Service
public class FeesServiceImpl implements IFeesService {

	@Autowired
	private FeesRepository feesRepository;

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Fees createStudentFees(Integer studentId, Integer courseId, Double finalFees, String date) {
		// TODO Auto-generated method stub
		
		Fees fees=new Fees(null, null, finalFees, LocalDate.parse(date));
		Fees findByStudent = feesRepository.findByStudent(studentRepository.findByStudentId(studentId));
		if(Objects.isNull(findByStudent)) {
		fees.setStudent(studentRepository.findById(studentId).get());
		fees.setCourse(courseRepository.findById(courseId).get());
		fees.setCreatedDate(LocalDate.now());
		fees.setUpdatedDate(LocalDate.now());
		return  feesRepository.save(fees);
		}
		throw new ResourceNotFoundException(AppConstants.DATA_ALREADY_EXIST);
	}

	@Override
	public PageResponse<FeesResponse> feesList(Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "feesId");
		Page<Fees> fees = feesRepository.findAllByIsCompleted(false,pageable);
		if(fees.getNumberOfElements()==0) {
			return new PageResponse<>(Collections.emptyList(), fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
		}
		List<FeesResponse> asList = Arrays.asList(mapper.map(fees.getContent(), FeesResponse[].class));
		return new PageResponse<>(asList, fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
	}

	@Override
	public FeesResponse findByFeesId(Integer feesId) {
		// TODO Auto-generated method stub
		Fees fees = feesRepository.findById(feesId).orElseThrow(()
				->new ResourceNotFoundException("Fees Not found for given Id!!"));
		return mapper.map(fees, FeesResponse.class);
	}

	@Override
	public List<FeesResponse> searchByName(String fullName) {

          List<Fees> findByStudent = feesRepository.findByStudentFullNameContaining(fullName);
          if(Objects.isNull(findByStudent)) {
        	  throw new ResourceNotFoundException("Student not found");
          }
          List<FeesResponse> asList = Arrays.asList(mapper.map(findByStudent, FeesResponse[].class));
		return asList;
	}

	@Override
	public List<FeesResponse> findFeesByDates(String startDate, String endDate) {

        List<Fees> findFeesByGivenDates = feesRepository.findFeesByGivenDates(LocalDate.parse(startDate),LocalDate.parse(endDate));
        if(Objects.isNull(findFeesByGivenDates)) {
      	  throw new ResourceNotFoundException("Fees is not found from given Dates");
        }
        List<FeesResponse> asList = Arrays.asList(mapper.map(findFeesByGivenDates, FeesResponse[].class));
		return asList;
	}

	@Override
	public PageResponse<FeesResponse> feesCompleteList(Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "feesId");
		Page<Fees> fees = feesRepository.findAllByIsCompleted(true,pageable);
		if(fees.getNumberOfElements()==0) {
			return new PageResponse<>(Collections.emptyList(), fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages() ,fees.isLast());
		}
		List<FeesResponse> asList = Arrays.asList(mapper.map(fees.getContent(), FeesResponse[].class));
		return new PageResponse<>(asList, fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
	}
	
	

}