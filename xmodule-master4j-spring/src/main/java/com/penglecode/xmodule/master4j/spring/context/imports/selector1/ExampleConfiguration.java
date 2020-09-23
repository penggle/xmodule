package com.penglecode.xmodule.master4j.spring.context.imports.selector1;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 21:10
 */
@Configuration
@Import(MessageServiceImportSelector.class)
public class ExampleConfiguration {

}
