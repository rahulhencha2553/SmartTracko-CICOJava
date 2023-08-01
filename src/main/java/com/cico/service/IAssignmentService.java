package com.cico.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Assignment;
import com.cico.payload.ApiResponse;

public interface IAssignmentService {

	Assignment getAssignment(Integer id);

	ApiResponse createAssignment(String question,String[] hints, MultipartFile[] images);

	ApiResponse deleteAssignment(Integer id);

	List<List<Assignment>> getAllAssignment();

	Assignment updateAssignment(Integer id, String question, String[] imagesId, MultipartFile[] images,String[] hints);

	Assignment updateAssignment1(Assignment assignment);
}
