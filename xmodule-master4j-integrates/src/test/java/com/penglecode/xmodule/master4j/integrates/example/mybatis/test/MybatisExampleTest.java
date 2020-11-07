package com.penglecode.xmodule.master4j.integrates.example.mybatis.test;

import com.penglecode.xmodule.master4j.integrates.example.mybatis.config.MybatisExampleConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/27 12:14
 */
@SpringBootTest(
        properties="spring.example.mybatis.enabled=true",
        classes={MybatisExampleConfiguration.class},
        webEnvironment=WebEnvironment.NONE
)
public class MybatisExampleTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void printSqlSessionFactory() {
        System.out.println(sqlSessionFactory);
    }

}
