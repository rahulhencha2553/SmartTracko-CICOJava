package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Chapter;
import com.cico.model.ChapterContent;
import com.cico.model.Subject;
import com.cico.service.IChapterService;

@RestController
@RequestMapping("/chapter")
@CrossOrigin("*")
public class ChapterController {
	
	@Autowired
	private IChapterService chapterService;
	
	@PostMapping("/addChapter")
	public ResponseEntity<Subject> addChapter( @RequestParam("subjectId")Integer subjectId,@RequestParam("chapterName") String chapterName,@RequestParam(name = "image" ,required = false)MultipartFile image){
		 Subject subject = chapterService.addChapter(subjectId,chapterName,image);	
		return new ResponseEntity<Subject>(subject,HttpStatus.OK);
	}
	
	@PostMapping("/addExamToChapter")
	public ResponseEntity<String> addExamToChapter(@RequestParam("subjectId") Integer subjectId,@RequestParam("examName")String examName){
		chapterService.addExamToChapter( subjectId,  examName);	
	return ResponseEntity.ok("Exam Added");
	}
	@PostMapping("/addChapterContent")
	public ResponseEntity<Chapter>addContentToChapter(@RequestParam("chapterId")Integer chapterId,@RequestParam("title")String title,@RequestParam("subTitle")String subTitle,@RequestParam("content")String content){
	Chapter chapter = chapterService.addContentToChapter(chapterId,title,subTitle,content);
	return new ResponseEntity<Chapter>(chapter,HttpStatus.OK);
	}
	
	@PutMapping("/updateChapterContent")
	public ResponseEntity<ChapterContent>updateChapterContent(@RequestParam("chapterId")Integer chapterId,@RequestParam("title")String title,@RequestParam("subTitle")String subTitle,@RequestParam("content")String content,@RequestParam("contentId")Integer contentId){
	 ChapterContent chapterContent = chapterService.updateChapterContent(chapterId,title,subTitle,content,contentId);
	return new ResponseEntity<ChapterContent>(chapterContent,HttpStatus.OK);
	}
	
	@GetMapping("/getChapterContent")
	public ResponseEntity<ChapterContent> getChapterContent(@RequestParam("chapterId") Integer chapterId,@RequestParam("chapterContentId")Integer chapterContentId){
	 ChapterContent chapterContent= chapterService.getChapterContent( chapterId,chapterContentId );	 
    return new ResponseEntity<ChapterContent>(chapterContent,HttpStatus.OK);
	}
	
	@PutMapping("/updateChapter")
	public ResponseEntity<Chapter> updateChapter(@RequestParam("chapterId")Integer chapterId,@RequestParam("subjectId")Integer subjectId,@RequestParam("chapterName")String chapterName){
		 Chapter chapter = chapterService.updateChapter(chapterId,subjectId,chapterName);	
	return  new ResponseEntity<Chapter>(chapter,HttpStatus.OK);
	}
	
	@GetMapping("/getChapterById")
	public ResponseEntity<Chapter> getChapterById(@RequestParam("chapterId") Integer chapterId){
	Chapter chapter=chapterService.getChapterById(chapterId);	
	return ResponseEntity.ok(chapter);
	}
	
	
	@PutMapping("/deleteChapterContent")
	public ResponseEntity<String> deleteChapterContent(@RequestParam("chapterId") Integer chapterId,@RequestParam("contentId")Integer contentId){
	 chapterService.deleteChapterContent(chapterId,contentId);	
	return new ResponseEntity<String>("Success",HttpStatus.OK);
	}
	
	@PutMapping("/deleteChapter")
	public ResponseEntity<String> deleteChapter(@RequestParam("chapterId") Integer chapterId,@RequestParam("subjectId")Integer subjectId){
	chapterService.deleteChapter(chapterId,subjectId);	
	return ResponseEntity.ok("Chapter Deleted");
	}
	
	@PutMapping("/updateChapterStatus")
	public ResponseEntity<String> updateChapterStatus(@RequestParam("chapterId") Integer chapterId){
	chapterService.updateChapterStatus(chapterId);	
	return ResponseEntity.ok("Chapter Updated");
	}
	
	@GetMapping("/getAllChapters")
	public ResponseEntity<List<Chapter>> getAllChapters(@RequestParam("subjectId")Integer subjectId){
		List<Chapter> chapters=chapterService.getAllChapters(subjectId);	
	return ResponseEntity.ok(chapters);
	}
	
	@GetMapping("/getChaptersBySubject")
	public ResponseEntity<List<Chapter>> getChaptersBySubject(@RequestParam("subjectId") Integer subjectId){
		List<Chapter> chapters=chapterService.getChaptersBySubject(subjectId);	
	return ResponseEntity.ok(chapters);
	}
	

}
