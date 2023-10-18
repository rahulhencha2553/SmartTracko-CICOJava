package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.DiscusssionForm;

@Repository
public interface DiscussionFormRepo extends JpaRepository<DiscusssionForm, Integer> {

}
