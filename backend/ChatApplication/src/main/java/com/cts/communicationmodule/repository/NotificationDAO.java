package com.cts.communicationmodule.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.communicationmodule.model.Notification;



@Repository
public interface NotificationDAO extends JpaRepository<Notification, String> {

	
}
