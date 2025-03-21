package com.cts.communicationmodule.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
 
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "forum_post")
public class Forum {
    
    @Id
    private String chatId;
 
    @Column(nullable = false)
    private String message;
 
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String courseId;
 
    private LocalDateTime timestamp;
 
    @PrePersist
    protected void onCreate() {
    	this.chatId=UUID.randomUUID().toString();
    	this.timestamp = LocalDateTime.now();
    }

	public Forum(String message, String userId, String courseId) {
		this.message = message;
		this.userId = userId;
		this.courseId = courseId;
	}
    
    
}
