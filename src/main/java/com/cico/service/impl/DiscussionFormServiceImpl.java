package com.cico.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.DiscussionFormComment;
import com.cico.model.DiscusssionForm;
import com.cico.model.Likes;
import com.cico.model.Student;
import com.cico.payload.CommentResponse;
import com.cico.payload.DiscussionFormResponse;
import com.cico.payload.LikeResponse;
import com.cico.repository.DiscussionFormCommentRepo;
import com.cico.repository.DiscussionFormRepo;
import com.cico.repository.LikeRepo;
import com.cico.repository.StudentRepository;
import com.cico.service.IFileService;
import com.cico.service.IdiscussionForm;

@Service
public class DiscussionFormServiceImpl implements IdiscussionForm {

	@Autowired
	private DiscussionFormRepo discussionFormRepo;
	@Autowired
	private IFileService fileService;

	@Autowired
	private StudentRepository studentRepository;;

	@Value("${discussionFromFile}")
	private String FILE_UPLAOD_DIR;

	@Autowired
	private DiscussionFormCommentRepo discussionFormCommentRepo;
	@Autowired
	private LikeRepo likeRepo;

	@Override
	public ResponseEntity<?> createDiscussionForm(Integer studentId, MultipartFile file, String content) {
		Student student = studentRepository.findById(studentId).get();
		if (Objects.nonNull(student)) {
			DiscusssionForm discusssionForm = new DiscusssionForm();
			discusssionForm.setCreatedDate(LocalDate.now());
			discusssionForm.setContent(content);
			discusssionForm.setStudent(student);
			if (Objects.nonNull(file)) {
				String savedFile = fileService.uploadFileInFolder(file, FILE_UPLAOD_DIR);
				discusssionForm.setFile(savedFile);
			}
			DiscusssionForm save = discussionFormRepo.save(discusssionForm);
			return new ResponseEntity<>(discussionFormFilter(save), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> createComment(Integer studentId, String content, Integer discussionFormId) {

		Student student = studentRepository.findById(studentId).get();
		Optional<DiscusssionForm> discussionForm = discussionFormRepo.findById(discussionFormId);
		if (Objects.nonNull(student)) {
			DiscussionFormComment comment = new DiscussionFormComment();
			comment.setCreatedDate(LocalDate.now());
			comment.setContent(content);
			comment.setStudent(student);
			DiscussionFormComment savedComment = discussionFormCommentRepo.save(comment);
			if (Objects.nonNull(discussionForm)) {
				List<DiscussionFormComment> comments = discussionForm.get().getComments();
				comments.add(savedComment);
				discussionForm.get().setComments(comments);
				discussionFormRepo.save(discussionForm.get());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> getAllDiscussionForm() {

		List<DiscusssionForm> list = discussionFormRepo.findAll();

		if (list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			List<DiscussionFormResponse> response = new ArrayList<>();
			list.forEach(obj -> {
				response.add(discussionFormFilter(obj));
			});

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getDiscussionFormById(Integer id) {
		Optional<DiscusssionForm> object = discussionFormRepo.findById(id);
		if (Objects.isNull(object)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(discussionFormFilter(object.get()), HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> addLike(Integer studentId, Integer discussionFormId) {
		Student student = studentRepository.findById(studentId).get();
		Optional<DiscusssionForm> discusssionForm = discussionFormRepo.findById(discussionFormId);
		if (Objects.nonNull(student) && Objects.nonNull(discusssionForm)) {
			List<Likes> likes = discusssionForm.get().getLikes();
			boolean anyMatch = likes.parallelStream().anyMatch(obj -> obj.getStudent().getStudentId() == studentId);
			if (!anyMatch) {
				Likes obj = new Likes();
				obj.setCreatedDate(LocalDate.now());
				obj.setStudent(student);
				likes.add(likeRepo.save(obj));
				discusssionForm.get().setLikes(likes);
				DiscusssionForm save = discussionFormRepo.save(discusssionForm.get());
				return new ResponseEntity<>(discussionFormFilter(save), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Already Liked !!", HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	public DiscussionFormResponse discussionFormFilter(DiscusssionForm obj) {
		List<CommentResponse> comments = new ArrayList<>();
		List<LikeResponse> likes = new ArrayList<>();
		DiscussionFormResponse object = new DiscussionFormResponse();
		object.setCreatedDate(obj.getCreatedDate());
		object.setContent(obj.getContent());
		object.setStudentName(obj.getStudent().getFullName());
		object.setStudentProfilePic(obj.getStudent().getProfilePic());
		object.setId(obj.getId());
		if (Objects.nonNull(obj.getLikes())) {
			obj.getLikes().forEach(obj1 -> {
				LikeResponse likeResponse = new LikeResponse();
				likeResponse.setCreatedDate(obj1.getCreatedDate());
				likeResponse.setStudentName(obj1.getStudent().getFullName());
				likeResponse.setStudentProfilePic(obj1.getStudent().getProfilePic());
				likeResponse.setId(obj1.getId());
				likes.add(likeResponse);
			});
		}
		if (Objects.nonNull(obj.getComments())) {
			obj.getComments().forEach(obj2 -> {
				CommentResponse commentResponse = new CommentResponse();
				commentResponse.setCreatedDate(obj2.getCreatedDate());
				commentResponse.setStudentName(obj2.getStudent().getFullName());
				commentResponse.setStudentProfilePic(obj2.getStudent().getProfilePic());
				commentResponse.setId(obj2.getId());
				commentResponse.setContent(obj2.getContent());
				comments.add(commentResponse);
			});
		}
		object.setLikes(likes);
		object.setComments(comments);
		return object;
	}

}
