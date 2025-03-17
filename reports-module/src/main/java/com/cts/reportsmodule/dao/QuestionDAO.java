package com.cts.reportsmodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Question;



public interface QuestionDAO extends JpaRepository<Question,String> {

}
