package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Question;
import com.cico.service.IQuestionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	IQuestionService questionService;
	
//	@PostMapping("/addQuestion")
//	public ResponseEntity<String> addQuestion(@RequestParam("question") String question, @RequestParam("options") List<String> options,
//			@RequestParam("image") MultipartFile image){
//		questionService.addQuestion(question,options,image);	
//	return ResponseEntity.ok("Question Added");
//	}
//	
	
	
	@PutMapping("/updateQuestion")
	public ResponseEntity<String> updateQuestion(@RequestBody Question question){
		questionService.updateQuestion(question);	
	return ResponseEntity.ok("Question Updated");
	}
	
	@GetMapping("/getQuestionById")
	public ResponseEntity<Question> getQuestionById(@RequestParam("questionId") Integer questionId){
	Question question=	questionService.getQuestionById(questionId);
	return ResponseEntity.ok(question);
	}
	
	
	@PutMapping("/deleteQuestion")
	public ResponseEntity<String> deleteQuestion(@RequestParam("questionId") Integer questionId){
		questionService.deleteQuestion(questionId);	
	return ResponseEntity.ok("Question Deleted");
	}
	
	@PutMapping("/updateQuestionStatus")
	public ResponseEntity<String> updateQuestionStatus(@RequestParam("questionId") Integer questionId){
		questionService.updateQuestionStatus(questionId);	
	return ResponseEntity.ok("Question Updated");
	}
	
	@GetMapping("/getAllQuestions")
	public ResponseEntity<List<Question>> getAllQuestions(){
		List<Question> questions=questionService.getAllQuestions();	
	return ResponseEntity.ok(questions);
	}
	
	@GetMapping("/getQuestionsByExam")
	public ResponseEntity<List<Question>> getQuestionsByExam(@RequestParam("examId") Integer examId){
		List<Question> questions=questionService.getQuestionsByExam(examId);	
	return ResponseEntity.ok(questions);
	}
}
