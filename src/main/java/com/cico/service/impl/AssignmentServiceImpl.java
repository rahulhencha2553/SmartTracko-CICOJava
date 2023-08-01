package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Assignment;
import com.cico.model.AssignmentImage;
import com.cico.payload.ApiResponse;
import com.cico.payload.AssignmentResponse;
import com.cico.repository.AssignmentImageRepository;
import com.cico.repository.AssignmentRepository;
import com.cico.service.IAssignmentService;
import com.cico.service.IFileService;
import com.cico.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssignmentServiceImpl implements IAssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private IFileService fileService;

	@Autowired
	private AssignmentImageRepository imageRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${workReportUploadPath}")
	private String filePath;

	@Override
	public Assignment getAssignment(Integer id) {
		Optional<Assignment> assignment = assignmentRepository.findById(id);

		if (!assignment.isPresent())
			throw new ResourceNotFoundException(AppConstants.NO_DATA_FOUND);
		return assignment.get();
	}

	@Override
	public ApiResponse createAssignment(String question, String[] hints, MultipartFile[] images) {
		Assignment assignment = new Assignment();
		assignment.setQuestion(question);
		System.out.println(images.toString());
		// setting images
		if (images != null) {
			List<AssignmentImage> list = assignment.getAssignmentImages();
			for (MultipartFile file : images) {
				if (file != null && !file.isEmpty()) {
					if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
						String fileName = fileService.uploadFileInFolder(file, filePath);
						AssignmentImage assignmentImage = new AssignmentImage();
						assignmentImage.setImageName(fileName);
						list.add(assignmentImage);
					}
				}
			}
			assignment.setAssignmentImages(list);
		}

		// setting hints
		if (hints != null) {
			List<String> list = assignment.getHints();
			for (String hint : hints) {
				list.add(hint);
			}
			assignment.setHints(list);
		}
		assignment.setIsDeleted(false);
		assignment.setCreatedTime(LocalDateTime.now());
		Assignment savedAssignment = assignmentRepository.save(assignment);
		return new ApiResponse(Boolean.TRUE, AppConstants.CREATE_SUCCESS, HttpStatus.CREATED);
	}

	// delete assignment
	@Override
	public ApiResponse deleteAssignment(Integer id) {
		Assignment assignment = assignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		assignment.setIsDeleted(true);
		assignmentRepository.save(assignment);
		return new ApiResponse(Boolean.TRUE, AppConstants.DELETE_SUCCESS, HttpStatus.OK);
	}

	@Override
	public List<List<Assignment>> getAllAssignment() {
		
		List<Assignment> list = assignmentRepository.findAll();
		List<List<Assignment>> response = new ArrayList<>();
	
		List<AssignmentResponse> assignmentResponses = new ArrayList<>();
		AssignmentResponse assignmentResponse = new AssignmentResponse();
		assignmentResponses.add(assignmentResponse);
		
		List<Assignment> assignmentslist = new ArrayList<>();
		
		int dayOfMonth = -1; // Initialize to an invalid value

		for (Assignment assignment : list) {
			int currentDayOfMonth = assignment.getCreatedTime().getDayOfMonth();

			if (currentDayOfMonth == dayOfMonth) {
				assignmentslist.add(assignment);
			} else {
				if (!assignmentslist.isEmpty()) {
					response.add(new ArrayList<>(assignmentslist));
				}
				dayOfMonth = currentDayOfMonth;
				assignmentslist = new ArrayList<>();
				assignmentslist.add(assignment);
				
			}
		}

		if (!assignmentslist.isEmpty()) {
			response.add(new ArrayList<>(assignmentslist));
		}                          

		for (List<Assignment> dayAssignments : response) {
			for (Assignment assignment : dayAssignments) {
				System.out.println(assignment);
			}
			System.out.println("\n\n");
		}

		return response;
	}

	// not working
	@Override
	public Assignment updateAssignment(Integer id, String question, String[] imagesId, MultipartFile[] images,
			String[] hints) {

		Assignment assignment = assignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));

		if (question != null)
			assignment.setQuestion(question);
		else
			assignment.setQuestion(assignment.getQuestion());

		List<AssignmentImage> imagesList = assignment.getAssignmentImages();
		if (imagesId != null && imagesId.length > 0) {

			for (int i = 0; i < imagesId.length; i++) {
				AssignmentImage assignmentImage = imageRepository.findById(Integer.parseInt(imagesId[i]))
						.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
				assignmentImage.setImageName(fileService.uploadFileInFolder(images[i], filePath));
				// imagesList.add(assignmentImage);
			}
		}
		if (hints != null) {
			List<String> list = assignment.getHints();
			for (int i = 0; i < hints.length; i++) {
				int value = Character.getNumericValue(hints[i].trim().charAt(0));
				list.remove(value);
				list.add(value, hints[i].substring(1));
			}
			assignment.setHints(list);
		}
		assignment.setAssignmentImages(imagesList);
		return assignmentRepository.save(assignment);
	}

	@Override
	public Assignment updateAssignment1(Assignment assignment) {
		Assignment assignment1 = assignmentRepository.findById(assignment.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));

		if (assignment.getQuestion() != null)
			assignment.setQuestion(assignment.getQuestion());
		else
			assignment.setQuestion(assignment.getQuestion());

		List<AssignmentImage> assignmentImages = assignment.getAssignmentImages();
		if (assignmentImages != null) {
			for (AssignmentImage image : assignmentImages) {
				AssignmentImage assignmentImage = imageRepository.findById(image.getId())
						.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
			}
		}
		return null;
	}
}
