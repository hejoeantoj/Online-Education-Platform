//package com.cts.communicationmodule.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.cts.communicationmodule.model.Forum;
//import com.cts.communicationmodule.service.ForumService;
//
//import java.util.List;
// 
//@RestController
//@RequestMapping("/forum")
//public class ForumController {
// 
//	@Autowired
//    private ForumService forumService;
//    
//   // Send message
//    @PostMapping("/send")
//    public Forum sendMessage(@RequestParam Long userId,
//                              @RequestParam Long courseId,
//                              @RequestParam String message) {
//        return forumService.sendMessage(userId, courseId, message);
//    }
// 
//    //Get all chat messages for course */
//    @GetMapping("/chat/{courseId}")
//    public List<Forum> getCourseChat(@PathVariable Long courseId) {
//        return forumService.getCourseChat(courseId);
//    }
//}
//


package com.cts.communicationmodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.communicationmodule.client.CourseClient;
import com.cts.communicationmodule.dto.ForumDTO;
import com.cts.communicationmodule.model.Forum;
import com.cts.communicationmodule.service.ForumService;
import com.cts.communicationmodule.utils.ResultResponse;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;
  
    
     
    // Send message
    @PostMapping("/send")
    public ResponseEntity<ResultResponse<Forum>> sendMessage(@RequestBody ForumDTO forumDTO) {
    	 	
        Forum forumMessage = forumService.sendMessage(forumDTO);

        ResultResponse<Forum> response = new ResultResponse<>();
        response.setSuccess(true);
        response.setMessage("Message sent successfully!");
        response.setData(forumMessage);
        response.setStatus(org.springframework.http.HttpStatus.OK);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    
    
    
    
    
  
    
    // Get all chat messages for course
    @GetMapping("/chat")
    public ResponseEntity<ResultResponse<List<Forum>>> getCourseChat(@RequestParam String courseId) {
        List<Forum> chatMessages = forumService.getCourseChat(courseId);

        ResultResponse<List<Forum>> response = new ResultResponse<>();
        response.setSuccess(true);
        response.setMessage("Chat messages retrieved successfully!");
        response.setData(chatMessages);
        response.setStatus(HttpStatus.OK);

        return ResponseEntity.ok(response);
    }
}
