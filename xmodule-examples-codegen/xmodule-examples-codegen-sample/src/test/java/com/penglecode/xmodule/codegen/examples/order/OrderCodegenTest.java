package com.penglecode.xmodule.codegen.examples.order;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.codegen.examples.boot.CodegenExampleApplication;
import com.penglecode.xmodule.common.codegen.controller.ControllerCodeGenerator;
import com.penglecode.xmodule.common.codegen.mybatis.MybatisCodeGenerator;
import com.penglecode.xmodule.common.codegen.service.ServiceCodeGenerator;
import com.penglecode.xmodule.common.util.ClassUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

/**
 * 自动代码生成示例
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/4 14:49
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes=CodegenExampleApplication.class)
public class OrderCodegenTest {

    @Test
    public void generateOrderMybatisCode() {
        new MybatisCodeGenerator("order").overrideCodegenConfig(config -> {
            //运行时指定Model.java代码输出位置
            config.getJavaModel().setTargetProject(Paths.get(config.getRuntimeProjectDir(),
                    "../xmodule-examples-codegen-modulea/src/main/java").toString());
            //运行时指定Mapper.xml代码输出位置(比如输出到隔壁project)
            config.getXmlMapper().setTargetProject(Paths.get(config.getRuntimeProjectDir(),
                    "../xmodule-examples-codegen-moduleb/src/main/java").toString());
            //运行时指定Mapper.java代码输出位置(比如输出到隔壁project)
            config.getDaoMapper().setTargetProject(Paths.get(config.getRuntimeProjectDir(),
                    "../xmodule-examples-codegen-moduleb/src/main/java").toString());
        }).execute();
    }

    @Test
    public void generateOrderServiceCode() {
        new ServiceCodeGenerator("order", new Class<?>[] {
                ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.Order"),
                ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.OrderDetail")
        }).overrideCodegenConfig(config -> {
            //运行时指定Model.class的扫描基准包
            config.setBasePackage(BasePackage.class.getPackage().getName());
            //运行时指定XxxService.java代码输出位置(比如输出到隔壁project)
            config.getJinterface().setTargetProject(Paths.get(config.getRuntimeProjectDir(),
                    "../xmodule-examples-codegen-moduleb/src/main/java").toString());
            //运行时指定XxxServiceImpl.java代码输出位置(比如输出到隔壁project)
            config.getJimplement().setTargetProject(Paths.get(config.getRuntimeProjectDir(),
                    "../xmodule-examples-codegen-moduleb/src/main/java").toString());
        }).execute();
    }

    @Test
    public void generateOrderControllerCode() {
        new ControllerCodeGenerator("order", new Class<?>[] {
                ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.Order"),
                ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.OrderDetail")
        }).overrideCodegenConfig(config -> {
            //运行时指定Model.class的扫描基准包
            config.setBasePackage(BasePackage.class.getPackage().getName());
            //运行时指定XxxController.java代码输出位置(比如输出到隔壁project)
            config.setTargetProject(Paths.get(config.getRuntimeProjectDir(),
                    "../xmodule-examples-codegen-moduleb/src/main/java").toString());
            config.setPrefixOfApiUrl("/api/ordercenter"); //接口URL前缀
            config.getDomainOfApiUrlMapping().put("com.penglecode.xmodule.ordercenter.model.Order", "order"); //接口URL的domain部分
            config.getDomainOfApiUrlMapping().put("com.penglecode.xmodule.ordercenter.model.OrderDetail", "order/detail"); //接口URL的domain部分
        }).execute();
    }

}
