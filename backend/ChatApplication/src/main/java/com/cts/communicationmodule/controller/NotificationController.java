package com.cts.communicationmodule.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.communicationmodule.model.Notification;
import com.cts.communicationmodule.service.NotificationService;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*", allowCredentials = "true")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/view")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = notificationService.getAllNotification();
        return ResponseEntity.ok(notifications);
    }
    
    @PostMapping("/create")
    public void createNewNotification(@RequestBody String message) {
         notificationService.createNotification(message);
    }
    
    

}




























//package com.cts.communicationmodule.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cts.communicationmodule.model.Notification;
//import com.cts.communicationmodule.service.NotificationService;
//
//
//@RestController
//@RequestMapping("/api/notification")
//public class NotificationController {
//    
//    @Autowired
//    private NotificationService notificationService;
//
//    @GetMapping("/view")
//    public ResponseEntity<List<Notification>> getNotifications() {
//        List<Notification> notifications = notificationService.getAllNotification();
//        return new ResponseEntity<>(notifications, HttpStatus.OK);
//    }
//    
//    @PostMapping("/create")
//    public ResponseEntity<Notification> createNewNotification(@RequestBody String message) {
//        Notification notification = notificationService.createNotification(message);
//        return new ResponseEntity<>(notification, HttpStatus.OK);
//    }
//    
//    
////    @ExceptionHandler(ResourceNotFoundException.class)
////    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
////        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
////    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGlobalException(Exception ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
//
