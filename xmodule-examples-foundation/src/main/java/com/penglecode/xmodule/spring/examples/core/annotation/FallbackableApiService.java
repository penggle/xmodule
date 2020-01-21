package com.penglecode.xmodule.spring.examples.core.annotation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ResponseMapping
public interface FallbackableApiService {

}
