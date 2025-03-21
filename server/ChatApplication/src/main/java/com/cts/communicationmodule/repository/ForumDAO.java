//package com.example.forum.repository;
//
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import com.example.forum.model.Enrollment;
//
//@Repository
//public class ForumRepository {
//
//	
//
//}
package com.cts.communicationmodule.repository; 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.communicationmodule.model.Forum;
 
public interface ForumDAO extends JpaRepository<Forum, String> {
   // List<Forum> findByCourseId(String courseId);
    
    
	 @Query("SELECT f FROM Forum f WHERE f.courseId = :courseId ORDER BY f.timestamp DESC")
	    List<Forum> findChatsByCourseId(@Param("courseId") String courseId);
	}
