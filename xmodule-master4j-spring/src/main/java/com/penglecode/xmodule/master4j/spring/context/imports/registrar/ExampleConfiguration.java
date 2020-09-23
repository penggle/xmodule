package com.penglecode.xmodule.master4j.spring.context.imports.registrar;

import com.penglecode.xmodule.master4j.spring.context.imports.common.MessageServiceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:12
 */
@Configuration
@Import({MessageServiceConfiguration.class, CompositeMessageServiceRegistrar.class})
public class ExampleConfiguration {


}
