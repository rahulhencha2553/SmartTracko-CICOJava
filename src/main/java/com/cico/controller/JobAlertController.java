package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.JobAlert;
import com.cico.payload.ApiResponse;
import com.cico.payload.JobAlertResponse;
import com.cico.payload.PageResponse;
import com.cico.service.IJobAlertService;
import com.cico.util.AppConstants;

@RestController
@RequestMapping("/job")
@CrossOrigin("*")
public class JobAlertController {

	@Autowired
	private IJobAlertService service;

	@PostMapping("/createJobApi")
	public ResponseEntity<ApiResponse> createJob(@RequestParam("jobTitle") String jobTitle,
			@RequestParam("technologyStackId") Integer technologyStackId,
			@RequestParam("jobDescription") String jobDescription, @RequestParam("companyName") String companyName,
			@RequestParam("experienceRequired") String experienceRequired, @RequestParam("type") String type,
			@RequestParam("jobPackage") String jobPackage, @RequestParam("technicalSkills") String technicalSkills) {
		JobAlert createJob = service.createJob(technologyStackId, jobTitle, jobDescription, companyName,
				experienceRequired, technicalSkills, type, jobPackage);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse(Boolean.TRUE, "Job Created Successfully", HttpStatus.CREATED));
	}

	@GetMapping("/getJobApi")
	public ResponseEntity<JobAlert> getJob(@RequestParam("jobId") Integer jobId) {
		JobAlert job = service.getJob(jobId);

		return new ResponseEntity<>(job, HttpStatus.OK);
	}

	@GetMapping("/searchJobApi")
	public ResponseEntity<List<JobAlert>> searchJobs(@RequestParam("field") String field,
			@RequestParam("role") String role) {
		List<JobAlert> searchJob = service.searchJob(field, role);

		return new ResponseEntity<>(searchJob, HttpStatus.OK);
	}

	@PutMapping("/activeJobApi")
	public ResponseEntity<JobAlert> activeJob(@RequestParam("jobId") Integer jobId) {
		JobAlert jobAlert = service.activeJob(jobId);
		return new ResponseEntity<>(jobAlert, HttpStatus.OK);
	}

	@PutMapping("/updateJobApi")
	public ResponseEntity<ApiResponse> updatejob(@RequestParam("jobId") Integer jobId,
			@RequestParam("TechnologyStackId") Integer TechnologyStack, @RequestParam("jobTitle") String jobTitle,
			@RequestParam("jobDescription") String jobDescription, @RequestParam("companyName") String companyName,
			@RequestParam("experienceRequired") String experienceRequired,
			@RequestParam("technicalSkills") String technicalSkills,
			@RequestParam("technologyStack") Integer technologyStackId) {
		JobAlert update = service.update(jobId, jobTitle, jobDescription, companyName, experienceRequired,
				technicalSkills, technologyStackId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse(Boolean.TRUE, "Job Updated Successfully", HttpStatus.CREATED));
	}

	@DeleteMapping("/deleteJobApi")
	public ResponseEntity<ApiResponse> delete(@RequestParam("jobId") Integer jobId) {
		service.delete(jobId);


		return new ResponseEntity<ApiResponse>(new ApiResponse(Boolean.TRUE, AppConstants.DELETE_SUCCESS, HttpStatus.OK),
				HttpStatus.OK);
	}

//	@GetMapping("/getAllJobsApi")
//	public ResponseEntity<JobAlert> getAllJobs(@RequestParam("page") Integer page, @RequestParam("size") Integer size,
//			@RequestParam("type") String type) {
//		Map<String, Object> allJobs = service.getAllJobs(page, size, type);
//		return new ResponseEntity<>(allJobs,HttpStatus.OK);
//	}

	@GetMapping("/getAllJobsApi")
	public PageResponse<JobAlertResponse> getAllJobs(
			@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam("type") String type) {

		return service.getAllJobAlert(page, size, type);
	}

	@GetMapping("/getAllJobsAndIntershipApi")
	public PageResponse<JobAlertResponse> getAllJobsAndIntership(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return service.getAllJobsAndIntership(page, size);
	}
}
