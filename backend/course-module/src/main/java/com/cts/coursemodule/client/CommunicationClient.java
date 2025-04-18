package com.cts.coursemodule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "COMMUNICATION-MODULE")
public interface CommunicationClient {
	
	@PostMapping("api/notification/create")
    public void createNewNotification(@RequestBody String message);

}
