package com.penglecode.xmodule.master4j.jvm.common.controller;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StackTraceController {

	@RequestMapping(value="/monitor/stacktrace")
	public String stackTrace(Model model) {
		Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
		allStackTraces = allStackTraces.entrySet().stream()
				.sorted(Comparator.comparing(entry -> entry.getKey().getName()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1, LinkedHashMap::new));
		model.addAttribute("allStackTraces", allStackTraces);
		return "monitor/stacktrace";
	}
	
}
