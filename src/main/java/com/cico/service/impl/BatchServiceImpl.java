package com.cico.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Batch;
import com.cico.payload.ApiResponse;
import com.cico.repository.BatchRepository;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.IBatchService;
import com.cico.util.AppConstants;


@Service
public class BatchServiceImpl implements IBatchService {
	
	public static final String BATCH_NOT_FOUND="BATCH NOT FOUND";
	
	@Autowired
    BatchRepository batchRepository;
	@Autowired
	private TechnologyStackRepository repository;
	@Override
	public Batch createBatch(Integer technologyStackId, String batchName, String batchStartDate, String batchEndDate) {
		// TODO Auto-generated method stub
		Batch batch=new Batch(batchName, LocalDate.parse(batchStartDate), LocalDate.parse(batchEndDate), null);
		batch.setTechnologyStack(repository.findById(technologyStackId).get());
		return batchRepository.save(batch);
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

	

}
