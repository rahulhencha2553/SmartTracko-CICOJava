package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Batch;
import com.cico.payload.ApiResponse;
import com.cico.service.IBatchService;
import com.cico.util.AppConstants;

@RestController
@RequestMapping("/batch")
@CrossOrigin("*")
public class BatchController {

	@Autowired
	IBatchService batchService;

	@PostMapping("/createBatch")
	public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {

		batch = batchService.createBatch(batch);
		return ResponseEntity.ok(batch);

	}

	@PutMapping("/updateBatch")
	public ResponseEntity<Batch> updateBatch(@RequestBody Batch batch) {

		batch = batchService.createBatch(batch);
		return ResponseEntity.ok(batch);

	}

	@PutMapping("/deleteBatch/{batchId}")
	public ResponseEntity<ApiResponse> deleteBatch(@PathVariable("batchId") Integer batchId) {
	ApiResponse response = batchService.deleteBatch(batchId);
	return ResponseEntity.ok(response);
	}

	@GetMapping("/getBatchById/{batchId}")
	public ResponseEntity<Batch> getBatchById(@PathVariable("batchId") Integer batchId) {
		Batch batch = batchService.getBatchById(batchId);
		return ResponseEntity.ok(batch);

	}

	@GetMapping("/getAllBatches")
	public ResponseEntity<List<Batch>> getAllBatches() {
		List<Batch> batches = batchService.getAllBatches();
		return ResponseEntity.ok(batches);

	}
  
	@GetMapping("/getUpcomingBatches")
	public ResponseEntity<List<Batch>> getUpcomingBatches() {
		List<Batch> batches = batchService.getUpcomingBatches();
		return ResponseEntity.ok(batches);

	}

	@PutMapping("/updateBatchStatus/{batchId}")
	public ResponseEntity<ApiResponse> updateBatchStatus(@PathVariable("batchId") Integer batchId) {
		ApiResponse response = batchService.updateBatchStatus(batchId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);

	}
}
