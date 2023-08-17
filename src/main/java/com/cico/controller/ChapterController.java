package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Chapter;
import com.cico.service.IChapterService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/chapter")
public class ChapterController {
	
	@Autowired
	private IChapterService chapterService;
	
//	@PostMapping("/addChapter")
//	public ResponseEntity<String> addChapter(@RequestParam("chapterName") String chapterName, @RequestParam("content") String content){
//		chapterService.addChapter(chapterName,content);	
//	return ResponseEntity.ok("Chapter Added");
//	}
	
	@PostMapping("/addExamToChapter")
	public ResponseEntity<String> addExamToChapter(@RequestParam("subjectId") Integer subjectId,@RequestParam("examName")String examName){
		chapterService.addExamToChapter( subjectId,  examName);	
	return ResponseEntity.ok("Exam Added");
	}
	
	@PutMapping("/updateChapter")
	public ResponseEntity<String> updateChapter(@RequestBody Chapter chapter){
		chapterService.updateChapter(chapter);	
	return ResponseEntity.ok("Chapter Updated");
	}
	
	@GetMapping("/getChapterById")
	public ResponseEntity<Chapter> getChapterById(@RequestParam("chapterId") Integer chapterId){
	Chapter chapter=chapterService.getChapterById(chapterId);	
	return ResponseEntity.ok(chapter);
	}
	
	
	@PutMapping("/deleteChapter")
	public ResponseEntity<String> deleteChapter(@RequestParam("chapterId") Integer chapterId){
	chapterService.deleteChapter(chapterId);	
	return ResponseEntity.ok("Chapter Deleted");
	}
	
	@PutMapping("/updateChapterStatus")
	public ResponseEntity<String> updateChapterStatus(@RequestParam("chapterId") Integer chapterId){
	chapterService.updateChapterStatus(chapterId);	
	return ResponseEntity.ok("Chapter Updated");
	}
	
	@GetMapping("/getAllChapters")
	public ResponseEntity<List<Chapter>> getAllChapters(){
		List<Chapter> chapters=chapterService.getAllChapters();	
	return ResponseEntity.ok(chapters);
	}
	
	@GetMapping("/getChaptersBySubject")
	public ResponseEntity<List<Chapter>> getChaptersBySubject(@RequestParam("subjectId") Integer subjectId){
		List<Chapter> chapters=chapterService.getChaptersBySubject(subjectId);	
	return ResponseEntity.ok(chapters);
	}
	

}
