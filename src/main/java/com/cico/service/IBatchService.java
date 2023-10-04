package com.cico.service;

import java.util.List;

import com.cico.model.Batch;
import com.cico.payload.ApiResponse;
import com.cico.payload.BatchRequest;


public interface IBatchService {


	ApiResponse deleteBatch(Integer batchId);

	Batch getBatchById(Integer batchId);

	List<Batch> getAllBatches();

	List<Batch> getUpcomingBatches();
	
	ApiResponse updateBatchStatus(Integer batchId);

	ApiResponse createBatch(BatchRequest request);

	ApiResponse updateBatch(Batch batch);

}
