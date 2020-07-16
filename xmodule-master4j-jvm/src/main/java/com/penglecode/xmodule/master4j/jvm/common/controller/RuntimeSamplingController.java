package com.penglecode.xmodule.master4j.jvm.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.master4j.jvm.common.model.RuntimeSample;
import com.penglecode.xmodule.master4j.jvm.common.service.RuntimeSampleService;

@RestController
@RequestMapping("/api/example")
public class RuntimeSamplingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeSamplingController.class);
	
	@Autowired
	private RuntimeSampleService runtimeSampleService;
	
	@GetMapping(value="/sampling/runtime", produces=MediaType.APPLICATION_JSON_VALUE)
	public Object samplingRuntime() {
		RuntimeSample sample = runtimeSampleService.collectRuntimeSample();
		LOGGER.debug(">>> Runtime sample : {}", sample);
		return sample;
	}
	
}
