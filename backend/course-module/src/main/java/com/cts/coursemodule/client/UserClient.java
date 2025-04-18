package com.cts.coursemodule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-MODULE")
public interface UserClient {
	
	@GetMapping("api/user/checkInstructor")
	public boolean checkInstructor(@RequestParam String userId);
	
	
	@GetMapping("api/user/checkStudent")
	public boolean checkStudent(@RequestParam String userId);

}
