package com.cico.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Batch;
import com.cico.model.Course;
import com.cico.payload.ApiResponse;
import com.cico.payload.BatchRequest;
import com.cico.repository.BatchRepository;
import com.cico.repository.CourseRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.IBatchService;
import com.cico.util.AppConstants;


@Service
public class BatchServiceImpl implements IBatchService {
	
	public static final String BATCH_NOT_FOUND="BATCH NOT FOUND";
	
	@Autowired
	private BatchRepository batchRepository;
	
	@Autowired
	private TechnologyStackRepository repository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	
	@Override
	public ApiResponse createBatch(BatchRequest request) {
		Course course = courseRepository.findById(request.getCourseId()).orElseThrow(()-> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		Batch batch=new Batch(request.getBatchName(), request.getBatchStartDate(), request.getBatchTiming(), request.getBatchDetails());
		batch.setSubject(subjectRepository.findBySubjectIdAndIsDeleted(request.getSubjectId(), false).get());
		List<Batch> batches = course.getBatches();
		batches.add(batch);
		course.setBatches(batches);
		Course course2 = courseRepository.save(course);
		if(Objects.nonNull(course2))
			return new ApiResponse(Boolean.TRUE, AppConstants.CREATE_SUCCESS , HttpStatus.CREATED);
		return new ApiResponse(Boolean.FALSE,AppConstants.FAILED,HttpStatus.OK);
		
	}
	

	@Override
	public ApiResponse deleteBatch(Integer batchId) {
		Batch batch = batchRepository.findById(batchId).orElseThrow(()->new ResourceNotFoundException(BATCH_NOT_FOUND));
		batch.setDeleted(true);
		batchRepository.save(batch);	
		return new ApiResponse(Boolean.TRUE, AppConstants.DELETE_SUCCESS, HttpStatus.OK);
	}

	@Override
	public Batch getBatchById(Integer batchId) {
		return 	batchRepository.findById(batchId).orElseThrow(()->new ResourceNotFoundException(BATCH_NOT_FOUND));
	}

	@Override
	public List<Batch> getAllBatches() {
		List<Batch> batches = batchRepository.findAll();
	 	if(batches.isEmpty())
	 		throw new ResourceNotFoundException(BATCH_NOT_FOUND);
	 	
	 	return batches;
	}

	@Override
	public List<Batch> getUpcomingBatches() {
		List<Batch> batches = batchRepository.findAllByBatchStartDate();
	 	if(batches.isEmpty())
	 		throw new ResourceNotFoundException(BATCH_NOT_FOUND);
	 	
	 	return batches;
	}

	@Override
	public ApiResponse updateBatchStatus(Integer batchId) {
	Batch batch = getBatchById(batchId);
		
		if(batch.isActive()==true)
			batch.setActive(false);
		
		else
			batch.setActive(true);
		
		batchRepository.save(batch);
		return new ApiResponse(Boolean.TRUE,AppConstants.SUCCESS,HttpStatus.OK);

	}


	@Override
	public ApiResponse updateBatch(Batch batch) {
		Batch save = batchRepository.save(batch);
		if(Objects.nonNull(save))
			return new ApiResponse(Boolean.TRUE, AppConstants.CREATE_SUCCESS, HttpStatus.CREATED);
		
		return new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK);
	}

	

}
