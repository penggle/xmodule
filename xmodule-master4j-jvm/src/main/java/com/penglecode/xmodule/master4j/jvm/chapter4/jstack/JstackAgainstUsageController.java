package com.penglecode.xmodule.master4j.jvm.chapter4.jstack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;

@RestController
@RequestMapping("/api/example/jstack")
public class JstackAgainstUsageController {

	@Autowired
	private JstackAgainstUsageExample example;
	
	@GetMapping(value="/try-endlessloop", produces=MediaType.APPLICATION_JSON_VALUE)
	public Object tryEndlessLoop() {
		example.tryEndlessLoop();
		return Result.success().build();
	}
	
	@GetMapping(value="/try-deadlock", produces=MediaType.APPLICATION_JSON_VALUE)
	public Object tryDeadLock() {
		example.tryDeadLock();
		return Result.success().build();
	}
	
}
