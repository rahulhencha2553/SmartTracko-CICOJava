package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.Likes;

@Repository
public interface LikeRepo extends JpaRepository<Likes, Integer> {
    
//	@Query("SELECT  l FROM LIKE  l WHERE l.student.studentId =:studentId AND l.d")
//	Optional<Likes> findByStudentIdAndDiscussionFormId(Integer studentId, Integer discussionFormId);

}
