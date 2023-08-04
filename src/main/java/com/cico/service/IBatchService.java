package com.cico.service;

import java.util.List;

import com.cico.model.Batch;
import com.cico.payload.ApiResponse;


public interface IBatchService {


	ApiResponse deleteBatch(Integer batchId);

	Batch getBatchById(Integer batchId);

	List<Batch> getAllBatches();

	List<Batch> getUpcomingBatches();
	
	ApiResponse updateBatchStatus(Integer batchId);

	Batch createBatch(Integer technologyStackId, String batchName, String batchStartDate, String batchEndDate);

}
