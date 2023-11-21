package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.service.IdiscussionForm;

@RestController
@CrossOrigin("*")
@RequestMapping("/discussionForm/")
public class DiscussionFormController {

	
	@Autowired
	private IdiscussionForm discussionFormService;

	@PostMapping("createDiscussionForm")
	public ResponseEntity<?> createDiscussionForm(@RequestParam("studentId")Integer studentId,@RequestParam(name = "file",required = false )MultipartFile file,@RequestParam("content")String content,@RequestParam(name ="audioFile",required = false )MultipartFile audioFile) {    
      return discussionFormService.createDiscussionForm(studentId,file,content,audioFile);
	}
    
	@PostMapping("createComment")
	public  ResponseEntity<?>createComment(@RequestParam("studentId")Integer studentId,@RequestParam("content")String content,@RequestParam("discussionFormId")Integer discussionFormId) {
		return  discussionFormService.createComment(studentId,content,discussionFormId);
	}
	
	@GetMapping("getAllDiscussionForm")
	public ResponseEntity<?>getAllDiscussionForm(@RequestParam("studentId")Integer studentId){
		return this.discussionFormService.getAllDiscussionForm(studentId);
	}
	
	@GetMapping("getDiscussionFormById")
	public ResponseEntity<?>getDiscussionFormById(@RequestParam("id")Integer id){
		return this.discussionFormService.getDiscussionFormById(id);
	}
	
	@PostMapping("addOrRemoveLike")
	public ResponseEntity<?>addLike(@RequestParam("studentId") Integer studentId,@RequestParam("discussionFormId")Integer discussionFormId ){
	  return	discussionFormService.addOrRemoveLike(studentId,discussionFormId);
	}
	
	@DeleteMapping("removeComment")
	public ResponseEntity<?>removeComment(@RequestParam("discussionFormId")Integer discussionFormId,@RequestParam("commentsId")Integer commentsId  ){
		  return	discussionFormService.removeComment(discussionFormId,commentsId);
		}
	
}
