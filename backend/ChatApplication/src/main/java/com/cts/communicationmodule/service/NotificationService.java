package com.cts.communicationmodule.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.communicationmodule.model.Notification;
import com.cts.communicationmodule.repository.NotificationDAO;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationDAO notificationDAO;

    public void createNotification(String message) {
        logger.info("Creating a new notification with message: {}", message);
        Notification notification = new Notification(message);
        Notification savedNotification = notificationDAO.save(notification);
        logger.info("Notification created successfully with ID: {}", savedNotification.getNotificationId());
        //return savedNotification;
    }
    
    

//	public void createNewNotificationForCourse(String message,String courseId) {
//		Notification notification=new Notification(message,courseId);
//		Notification savedNotification=notificationDAO.save(notification);	
//	}
    
    
    
     
    public List<Notification> getAllNotification() {
        logger.info("Fetching all notifications from the database.");
        List<Notification> notifications = notificationDAO.findAll();
        logger.info("Retrieved {} notifications.", notifications.size());
        return notifications;
    }
    
    
//    public List<Notification> getAllNotificationForCourse(String courseId){
//    	List<Notification> notifications=notificationDAO.findByCourseId(courseId);
//    	return notifications;
//    }









}
