package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Subject;
import com.cico.service.ISubjectService;



@RestController
@RequestMapping("/subject")
public class SubjectController {
	
	@Autowired
	private ISubjectService subjectService;
	
	@PostMapping("/addSubject")
	public ResponseEntity<String> addSubject(@RequestParam("subjectName") String subjectName){
	subjectService.addSubject(subjectName);	
	return ResponseEntity.ok("Subject Added");
	}
	
	@PostMapping("/addChapterToSubject")
	public ResponseEntity<String> addChapterToSubject(@RequestParam("subjectId") Integer subjectId,@RequestParam("chapterName") String chapterName, @RequestParam("content") String content){
	subjectService.addChapterToSubject(subjectId,chapterName,content);	
	return ResponseEntity.ok("Chapter Added");
	}
	
	@PutMapping("/updateSubject")
	public ResponseEntity<String> updateSubject(@RequestBody Subject subject){
	subjectService.updateSubject(subject);	
	return ResponseEntity.ok("Subject Updated");
	}
	
	@GetMapping("/getSubjectById")
	public ResponseEntity<Map<String,Object>> getSubjectById(@RequestParam("subjectId") Integer subjectId){
		Map<String,Object> map=subjectService.getSubjectById(subjectId);	
	return ResponseEntity.ok(map);
	}
	
	
	@PutMapping("/deleteSubject")
	public ResponseEntity<String> deleteSubject(@RequestParam("subjectId") Integer subjectId){
	subjectService.deleteSubject(subjectId);	
	return ResponseEntity.ok("Subject Deleted");
	}
	
	@PutMapping("/updateSubjectStatus")
	public ResponseEntity<String> updateSubjectStatus(@RequestParam("subjectId") Integer subjectId){
	subjectService.updateSubjectStatus(subjectId);	
	return ResponseEntity.ok("Subject Updated");
	}
	
	@GetMapping("/getAllSubjects")
	public ResponseEntity<List<Subject>> getAllSubjects(){
		List<Subject> subjects=subjectService.getAllSubjects();	
	return ResponseEntity.ok(subjects);
	}
	

}