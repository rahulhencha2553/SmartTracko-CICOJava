package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
			discusssionForm.setCreatedDate(LocalDateTime.now());
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
			comment.setCreatedDate(LocalDateTime.now());
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
		System.err.println("1111");
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
	public ResponseEntity<?> addOrRemoveLike(Integer studentId, Integer discussionFormId) {
		System.out.println(studentId);
		System.out.println(discussionFormId);

		Optional<Student> student1 = studentRepository.findById(studentId);
		Optional<DiscusssionForm> discusssionForm = discussionFormRepo.findById(discussionFormId);
		if (Objects.nonNull(student1) && Objects.nonNull(discusssionForm)) {
			DiscusssionForm form = discusssionForm.get();
			Student  student = student1.get();
			List<Likes> likes = form.getLikes();
			Likes like = new Likes();
			if (!likes.isEmpty()) {
				like = likes.parallelStream().filter(obj -> obj.getStudent().getStudentId() == studentId).findFirst()
						.orElse(null);
			}
			if (Objects.isNull(like)) {
				Likes obj = new Likes();
				obj.setCreatedDate(LocalDateTime.now());
				obj.setStudent(student);
				likes.add(likeRepo.save(obj));
				form.setLikes(likes);
				DiscusssionForm save = discussionFormRepo.save(form);
				return new ResponseEntity<>(discussionFormFilter(save), HttpStatus.OK);
			} else {
				form.setLikes(likes.parallelStream().filter(obj -> obj.getStudent().getStudentId() != studentId)
						.collect(Collectors.toList()));
				discussionFormRepo.save(form);
				int deleteLike = likeRepo.deleteLike(student);
				if (deleteLike != 0)
					return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
				else
					return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
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
		object.setFile(obj.getFile());
		object.setCourseName(obj.getStudent().getApplyForCourse());
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

	@Override
	public ResponseEntity<?> removeComment(Integer discussionFormId,Integer commentsId) {
		  
		Optional<DiscusssionForm> findById = discussionFormRepo.findById(discussionFormId);
		if(findById.isPresent()) {
		discussionFormCommentRepo.deleteById(commentsId);
		}
		return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
	}

}
