package com.cts.communicationmodule.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.communicationmodule.client.CourseClient;
import com.cts.communicationmodule.dto.ForumDTO;
import com.cts.communicationmodule.exception.CourseNotFoundException;
import com.cts.communicationmodule.exception.UserNotFoundException;
import com.cts.communicationmodule.model.*;
import com.cts.communicationmodule.repository.*;

import java.util.List;
 
@Service

public class ForumService {
 
	@Autowired
    private ForumDAO forumDAO;
	
	
	 @Autowired
	private CourseClient courseClient;
    
	 	 
	//Send message
    @Transactional
    public Forum sendMessage(ForumDTO forumDTO) {
    	
    	boolean isCourseVerified = courseClient.verifyCourse(forumDTO.getCourseId().toString());
    	if (!isCourseVerified) {
    	    throw new CourseNotFoundException("No course found");
    	}

    	String userId = forumDTO.getUserId().toString();
    	List<String> enrollmentList = courseClient.studentList(forumDTO.getCourseId().toString());
        
    	
    	if (userId.equals(courseClient.getInstructorId(forumDTO.getCourseId().toString()))) {
    		Forum post = new Forum();
        	post.setUserId(forumDTO.getUserId().toString());
        	post.setCourseId(forumDTO.getCourseId().toString());
        	post.setMessage(forumDTO.getMessage());
            return forumDAO.save(post);
    	}else if (enrollmentList.contains(userId)) {
    		Forum post = new Forum();
        	post.setUserId(forumDTO.getUserId().toString());
        	post.setCourseId(forumDTO.getCourseId().toString());
        	post.setMessage(forumDTO.getMessage());
            return forumDAO.save(post);   	    
    	}else {
    		throw new UserNotFoundException("User does not belong to this course");
    	}
    
    }
 
  //Get chat history for course 
    public List<Forum> getCourseChat(String courseId) {
        return forumDAO.findChatsByCourseId(courseId);
    }
}
