package com.cts.reportsmodule.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="QUIZ-MODULE")
public interface QuizClient {
	
	 @GetMapping("/api/quiz/QuizDetails")
	    public List<String> getAllQuizByCourseId(@RequestParam String courseId);
	 
	 @GetMapping("/api/squiz/QuizSubmissionDetails")
	    public Map<String,Double> getQuizByStudentIdAndQuizId(@RequestParam String studentId,@RequestParam String quizId);
}
